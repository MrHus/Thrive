(ns thrive.human
  (:use thrive.actor)
  (:use [thrive.cell :only (closed-cell-with-food, closed-unknown-cells, find-cell-loc, find-cell, find-cells, cells-with-food, unknown-cells surrounding-cells-by-mask)])
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
(def traversable {:grass 1, :forest 2, :mountain 3, :desert 2 :sea 5, :unknown 25, :lava false})
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
  [^Human p, world]
  (if (= [(:x p) (:y p)] (:city p))
    (if (= (:action p) :scout)
      (rand-nth (unknown-cells world))
      (first (cells-with-food world)))
    {:x ((:city p) 0) :y ((:city p) 1)}))

(defn observe
  "A human can observe left, right up, and down. This function alters
   the world as the person sees it with the actual cells from the world."
  [^Human p, actual-world world-size]
  (let [observed-cells (map #(assoc % :time (System/currentTimeMillis)) (find-cells actual-world (surrounding-cells-by-mask (:x p) (:y p) observe-mask world-size) world-size))
        p-world (reduce #(assoc %1 (find-cell-loc (:x %2) (:y %2) world-size) %2) (:world p) observed-cells)]
    (assoc p :world p-world)))

(defn ^Human think
  "A human decides a course of action based on his current state.
   Still needs to have/obtain a destination (currently set for the city).
   Then walk function needs to be updated so that Human moves to the first
   location stored in the movement/detail-plan/walk-path vector"
  [^Human p, world-size]
  (if (empty? (:movement p))
    (if (>= (:food p) max-food-in-backpack)
      (assoc p :action :city :movement (get-plan (:planner p) (:x p) (:y p) ((:city p) 0) ((:city p) 1) movement traversable (:world p) world-size))
      (let [closed-food-cell (closed-cell-with-food  [(:x p) (:y p)]  (:world p))]
        (if (empty? closed-food-cell)
          (let [action :scout
                closed-unknow-cell (closed-unknown-cells [(:x p) (:y p)] (:world p))]
            (let
              [scout-route (get-plan (:planner p) (:x p) (:y p) (:x closed-unknow-cell) (:y closed-unknow-cell) movement traversable (:world p) world-size)]
              (assoc p :action action :movement scout-route)))
          (let [food-path (get-plan (:planner p) (:x p) (:y p) (:x closed-food-cell) (:y closed-food-cell) movement traversable (:world p) world-size)]
            (assoc p :action :scavenge-food :movement food-path)
            ))))
    p))

(defn walk  
  [^Human p world-size]
  (let [step (first (:movement p))]
    (if (is-move-valid? p step (:world p) world-size)
      (assoc p :x (step 0) :y (step 1) :movement (rest (:movement p)))
      (assoc p :movement []))))

(defn is-alive?
  [^Human p]
  (> (:food p) 0))

(defn ^Human live-human
  "A human observes his surroundings, thinks up a dicision and then acts accordingly."
  [^Human p, actual-world world-size]
  (time (walk (think (observe p actual-world world-size) world-size) world-size)))

(extend-type Human
  Actor
  (live [this world world-size] (live-human this (:cells @world) world-size))
  (interval [this] 1000)
  (alive? [this] (is-alive? this)))
