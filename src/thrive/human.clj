(ns thrive.human
  (:use thrive.actor)
  (:use [thrive.cell :only (find-cell-loc, find-cell, find-cells, cells-with-food)])
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

(def world-width-height 3)

;; What is visible by the Person format is [x, y]
(def visibility-matrix
	[[0 0] [-1 0] [1 0] [0 -1] [0 1]])

(defn visibles
  "Gets the visible cell's of a specific cell, but within the bounds of the world.
   Returns [{:x, :y} ...] of all cells that are visible"
  [x, y, bounds]
  (filter
    #(and (>= (:x %) 0) (< (:x %) bounds) (>= (:y %) 0) (< (:y %) bounds))
	(map 
		#(let [dx (first %)
			     dy (last %)]
			  {:x (+ x dx) :y (+ y dy)}) visibility-matrix)))
	
(defn observe
  "A human can observe left, right up, and down. This function alters
   the world as the person sees it with the actual cells from the world."
  [^Human p, actual-world]
  (let [observed-cells (find-cells actual-world (visibles (:x p) (:y p) world-width-height) world-width-height)
        p-world (reduce #(assoc %1 (find-cell-loc (:x %2) (:y %2) world-width-height) %2) (:world p) observed-cells)]
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

(defn ^Human live-human
  "A human first observers his surroundings than makes a move."
  [^Human p, actual-world] 
  (walk (observe p actual-world)))

(extend-type Human
  Actor
  (live [this world] (live-human this (:cells @world)))
  (interval [this] 1000))
