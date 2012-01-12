(ns thrive.human
  (:use thrive.actor)
  (:use [thrive.cell :only (find-cell-loc, find-cell, find-cells, cells-with-food, surrounding-cells-by-mask)])
  (:gen-class))
  
(defrecord Human
[
  x 	  ;; The x position on the world
  y 	  ;; The y position on the world
  z	    ;; The z position on the world, is equal to height of the tile.
  food  ;; The current food this person is carrying.
  world ;; The current world as this person sees it.
  city  ;; The city coordinates that this person calls home.
])
    
(def movement {:left [0, -1], :right [0, 1], :up [-1, 0], :down [1, 0]})
(def traversable {:city 1, :grass 1, :mountain 3, :sea 15, :lava false})

(defn is-move-valid?
  "Takes the current position of the human (x, y, z).
   And takes the proposed new position (dx, dy) and checks if the
   move is valid. A move is invalid when it lands on :lava and
   when difference of z and dz is more one.
   It is assumed that the dx and dy are neighbors of x and y"
  ([^Human p dx dy world world-size]
   (is-move-valid? (:x p) (:y p) (:z p) dx dy world world-size))
  ([x y z dx dy world world-size]
    (let [^Cell dest (find-cell dx dy world world-size)]
      (and (not= :lava (:tile dest)) (> 1 (- (:z dest) z))))))

;; What is visible by the Person format is [x, y]
(def observe-mask [[0 0] [-1 0] [1 0] [0 -1] [0 1]])
	
(defn observe
  "A human can observe left, right up, and down. This function alters
   the world as the person sees it with the actual cells from the world."
  [^Human p, actual-world world-size]
  (let [observed-cells (find-cells actual-world (surrounding-cells-by-mask (:x p) (:y p) observe-mask world-size) world-size)
        p-world (reduce #(assoc %1 (find-cell-loc (:x %2) (:y %2) world-size) %2) (:world p) observed-cells)]
    (assoc p :world p-world)))

(defn closed-location
  "find closed location to the human"
  [locations, p]
  (let [first-location (first locations) second-location (second locations)]
    first-location))

(defn ?
  [a]
  (if (> a 0)
    1
    (if (< a 0)
      -1
      0)))

(defn path-finding-a*
  "Calcuate a path from the first point to the second point on the third given world"
  [current, finish, world]
  
  )

(defn manhattan-distance [[x1 y1] [x2 y2]]
  (+ (Math/abs ^Integer (- x2 x1)) (Math/abs ^Integer (- y2 y1))))

;((defn cost 
;   "g(x) - cost of getting to that node from starting node. 
;    h(x) - cost of getting to the goal node from current node.
;    f(x) - g(x)+h(x) "
;   [curr start end]
;  (let [g (manhattan-distance start curr) 
;        h (manhattan-distance curr end)   
;        f (+ g h)]
;    [f g h])))   

(defn ^Human walk
  "A human moves to the right every loop. Need to update that user uses algorithm"
  [^Human p]
  (let [locations (cells-with-food (:world p))]
    (if (> (count locations) 0)
      (let [closed-location (closed-location locations p)
            p-x (+ (:x p) (?(- (:x closed-location) (:x p))))
            p-y (+ (:y p) (?(- (:y closed-location) (:y p))))]
        (assoc (assoc p :x p-x) :y p-y))
      (let [p-x (+ (:x p) 1)]
        (assoc p :x p-x)))))

(defn ^Human right-walk
  "Walks to the right. Is used to test stuff."
  [^Human p world-size]
  (if (is-move-valid? p (inc (:x p)) (:y p) (:world p) world-size)
    (assoc p :x (inc (:x p)))
    p))

(defn ^Human live-human
  "A human first observers his surroundings than makes a move."
  [^Human p, actual-world world-size] 
  (right-walk (observe p actual-world world-size) world-size))

(extend-type Human
  Actor
  (live [this world world-size] (live-human this (:cells @world) world-size))
  (interval [this] 1000))
