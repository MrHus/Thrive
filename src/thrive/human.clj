(ns thrive.human
  (:use thrive.actor)
  (:gen-class))
  
(defrecord Human
[
  x 	;; The x position on the world
  y 	;; The y position on the world
  z	    ;; The z position on the world, is equal to height of the tile.
  food  ;; The current food this person is carrying.
  world ;; The current world as this person sees it.
])

(def world-width-height 3)

(defn find-cells
  "Find all cells in the actual world from the coll, which is a collection of [{:x, :y}]
   
   To understand this function lets look at the math:
   
   The vector contains each cell at a specific location. For instance in this 2*2 world:
   [(0, 0), (1, 0), (0, 1), (1, 1)]
   
   The formula for getting the 'location' of a cell while knowing the cells x and y is:
   world-width-height   = 1 = max [x | cell-from-world]
   location(cell)       = cell(y) * world-width-height + cell(x)
   location(cell(1, 1)) = 1 * 2 + 1 = 4
   
   Note that the world has to be a perfect square."
  [actual-world coll]
  (map #(nth actual-world %) (map #(+ (:x %) (* world-width-height (:y %))) coll)))

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
  (let [observed-cells (find-cells actual-world (visibles (:x p) (:y p) world-width-height))
        p-world (reduce #(assoc %1 (+ (:x %2) (* world-width-height (:y %2))) %2) (:world p) observed-cells)]
    (assoc p :world p-world)))

(defn ^Human move
  "A human moves to the right every loop. Need to update that user uses algorithm"
  [^Human p]
  (let [p-x (+ :x 1)]
  (assoc p :x p-x)))

(defn ^Human live-human
  "A human first observers his surroundings than makes a move."
  [^Human p, actual-world] 
     (do
       (observe p actual-world)
       (println p " \nd")
       (move p)))
  
(extend-type Human
  Actor
  (live [this world] (live-human this (:cells @world)))
  (interval [this] 1000))