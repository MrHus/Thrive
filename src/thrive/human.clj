(ns thrive.human
  (:use thrive.actor)
  (:use [thrive.city :only (deliver-food)])
  (:import (thrive.city City))
  (:use [thrive.cell :only (closest-cell-with-food, closest-unknown-cells, find-cell-loc, find-cell, find-cells, cells-with-food, unknown-cells surrounding-cells-by-mask)])
  (:use [thrive.planner :only (get-plan)])
  (:gen-class))

(defrecord Human
[
  x        ;; The x position on the world
  y        ;; The y position on the world
  z        ;; The z position on the world, is equal to height of the tile.
  food     ;; The current food this person is carrying.
  world    ;; The current world as this person sees it.
  city     ;; The city coordinates that this person calls home.
  action   ;; The action the human is thinking of
  movement ;; The movement the human needs to walk to reach the destination
  planner  ;; Defines which pathfinding algorithm the human knows
])

(def max-food-in-backpack 50)
(def movement [[0, 0], [0, -1], [0, 1], [-1, 0], [1, 0]])
(def traversable {:grass 1, :forest 2, :mountain-1 2, :mountain-2 3, :mountain-3 4, :desert 2 :sea 5, :unknown 25, :lava false, :not-reachable false})
(def actions [:scavenge-food, :scout, :hungry-scout, :hungry, :city])
(def observe-mask [[0 0] [-1 0] [1 0] [0 -1] [0 1]]);; What is visible by the Person, format is [x, y]

(defn is-move-valid?
  "Takes the current position of the human (x, y, z).
   And takes the proposed new position (dx, dy) and checks if the
   move is valid. A move is invalid when traversable for that :tile
   returns false.
   It is assumed that the dx and dy are neighbors of x and y"
  ([^Human p [dx dy] world world-size]
   (is-move-valid? (:x p) (:y p) (:z p) dx dy world world-size))
  ([x y z dx dy world world-size]
    (let [^Cell dest (find-cell dx dy world world-size)]
      (not= false (traversable (:tile dest))))))

(defn find-destination
  "Obtains a new destination for the human."
  [^Human p, world]
  (if (= [(:x p) (:y p)] (:city p))
    (if (= (:action p) :scout)
      (rand-nth (unknown-cells world))
      (first (cells-with-food world)))
    {:x ((:city p) 0) :y ((:city p) 1)}))

(defn digest
  "A human eats food from it's own stock if it is not in the city."
  [^Human p]
  (let [city-x ((:city p) 0)
        city-y ((:city p) 1)]
    (if (not= [(:x p) (:y p)] [city-x city-y])
      (assoc p :food (dec (:food p)))
      p)))

(defn move
  "Allows humans to move to a new cell."
  [^Human p, world-size]
  (let [step (first (:movement p))]
    (if (is-move-valid? p step (:world p) world-size)
      (assoc p :x (step 0) :y (step 1) :movement (rest (:movement p)))
      (assoc p :movement []))))

(defn observe
  "A human can observe left, right up, and down. This function alters
   the world as the person sees it with the actual cells from the world."
  [^Human p, world world-size]
  (let [actual-world (:cells world)
        observed-cells (map #(assoc % :time (System/currentTimeMillis)) (find-cells actual-world (surrounding-cells-by-mask (:x p) (:y p) observe-mask world-size) world-size))
        p-world (reduce #(assoc %1 (find-cell-loc (:x %2) (:y %2) world-size) %2) (:world p) observed-cells)]
    (assoc p :world p-world)))

(defn get-city
  "Returns the hometown agent of the human."
  [^Human p, world]
  (let [hometown (:city p)
        city (filter #(and (instance? City @%) (= (hometown 0) (:x @%)) (= (hometown 1) (:y @%))) (:actors world))]
    city))

(defn in-hometown?
  [^Human p]
  (= [(:x p) (:y p)] (:city p)))

(defn backpack-is-full?
  [^Human p]
  (>= (:food p) max-food-in-backpack))

(defn set-unreachable
  [^Human p x y world-size]
  (let [cell-location (find-cell-loc x y world-size)
        old-cell ((:world p) cell-location)
        new-cell (assoc old-cell :tile :not-reachable :time (System/currentTimeMillis))]
    (println old-cell " : " new-cell)
    (assoc p :world (assoc-in (:world p) [cell-location] new-cell))))

(defn scout-plan
  "Plan to the unknown or just a random place if everything is known."
  [^Human p world-size]
  (let [closest-unknown-cell (closest-unknown-cells [(:x p) (:y p)] (:world p))]
    (if (empty? closest-unknown-cell)
      (get-plan (:planner p) (:x p) (:y p) (int (rand world-size)) (int (rand world-size)) movement traversable (:world p) world-size))  
      (get-plan (:planner p) (:x p) (:y p) (:x closest-unknown-cell) (:y closest-unknown-cell) movement traversable (:world p) world-size)))  

(defn ^Human plan-to-closest-food-or-scout
  "Try to plan to the closest food resource. If no food is found just scout."
  [^Human p world-size]
  (let [closest-food-cell (closest-cell-with-food [(:x p) (:y p)] (:world p))]
    (if (empty? closest-food-cell)
      ;plan route to random unknown cell :scout
      (let [action :scout
            scout-route (scout-plan p world-size)]
        (println "Plan " "x " (:x p) " y " (:y p) " route " scout-route " zero? " (zero? (count scout-route)))
        (if (zero? (count scout-route))
          (let [closest-unknown-cell (closest-unknown-cells [(:x p) (:y p)] (:world p))
                new-human (assoc (set-unreachable p (:x closest-unknown-cell) (:y closest-unknown-cell) world-size) :action action :movement scout-route)]
            (println new-human)
            (println (closest-unknown-cells [(:x p) (:y p)] (:world p)))
            (println (closest-unknown-cells [(:x new-human) (:y new-human)] (:world new-human)))
            new-human)
            (assoc p :action action :movement scout-route)))
      ;plan route to nearest food stash - :scavenge-food
      (let [food-path (get-plan (:planner p) (:x p) (:y p) (:x closest-food-cell) (:y closest-food-cell) movement traversable (:world p) world-size)]
        (assoc p :action :scavenge-food :movement food-path)))))

(defn ^Human back-pack-full-thought
  "This thought describes what the human thinks when his backpack is full"
  [^Human p ref-world world-size]
  (if (in-hometown? p)
    ;Drop food in city
    (let [amount (- (:food p) max-food-in-backpack)
          city (get-city p @ref-world)]    
      ;(send-off city deliver-food @city amount)
      (plan-to-closest-food-or-scout (assoc p :food (- (:food p) amount))))
    ;plan route to city to drop of food
    (assoc p :action :city :movement (get-plan (:planner p) (:x p) (:y p) ((:city p) 0) ((:city p) 1) movement traversable (:world p) world-size))))
  
(defn grabbed-food-from-cell
  "How many food did the human grab from a cell.
   Grab all if backpack is empty grab limited amount when half full"
  [^Human p cell]
  (let [roomleft (- max-food-in-backpack (:food p))]
    (if (>= (:food cell) roomleft) 
      roomleft 
      (- roomleft (:food cell)))))
  
(defn ^Human back-pack-halffull-thought
  "This thought describes what the human thinks when the backpack is half full."  
  [^Human p current-cell ref-world world-size]
  ;; My backpack is not filled with food. 
  (if (> (:food current-cell) 0)
    ;Pickup food then plan to city
    (let [grabbed-food (grabbed-food-from-cell p current-cell)]
      (println "Grabbed food " grabbed-food " from world ") 
      ;; Alter the cells food value since I'm going to pick it up. 
      ;(let [cells  (:cells @ref-world)
            ;loc    (find-cell-loc (:x p) (:y p) world-size)
            ;cell   (find-cell (:x p) (:y p) @ref-world world-size)]
        ;(println cells loc cell)    
        ;(dosync 
          ;(alter ref-world assoc :cells 
            ;(assoc cells loc 
              ;(assoc cell :food (- (:food current-cell) grabbed-food))))))
      (assoc p 
        :action :city 
        :movement (get-plan (:planner p) (:x p) (:y p) ((:city p) 0) ((:city p) 1) movement traversable (:world p) world-size) 
        :food (+ (:food p) grabbed-food)))
    (plan-to-closest-food-or-scout p world-size)))
    
(defn ^Human think
  "A human decides a course of action based on his current state."
  [^Human p, ref-world, world-size]
  ;; There is nothing to do (could be in city)
  (if (empty? (:movement p))
    (if (backpack-is-full? p)
      (do 
        (println "full back pack")
        (back-pack-full-thought p ref-world world-size))
      (do 
        (println "not full back pack")
        (back-pack-halffull-thought p (find-cell (:x p) (:y p) (:world p) world-size) ref-world world-size)))
    p))
    
(defn act
  "A human acts after thinking up an action like move or wait."
  [^Human p, world-size]
  (let [current-cell (find-cell (:x p) (:y p) (:world p) world-size)
        cell-type (:tile current-cell)
        move-cost (cell-type traversable)]
    (if (> move-cost 1)
      (loop [h p i (dec move-cost)]
        (if (zero? (:food h))
          h
          (if (zero? i)
            (move h world-size)
            (do
              (. Thread sleep (interval h))
              (recur (digest h) (dec i))))))
      (move p world-size))))

(defn is-alive?
  "Condition of a human to stay alive."
  [^Human p]
  (> (:food p) 0))

(defn ^Human live-human
  "A human observes his surroundings, thinks up a dicision and then acts accordingly."
  [^Human p, ref-world world-size]
  (time (digest (act (think (observe p @ref-world world-size) ref-world world-size) world-size))))

(extend-type Human
  Actor
  (live [this world world-size] (live-human this world world-size))
  (interval [this] 1000)
  (alive? [this] (is-alive? this)))
