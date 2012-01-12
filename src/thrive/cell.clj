(ns thrive.cell
  (:use thrive.actor)
  (:gen-class))

(defrecord Cell
[
  x     ;; The x position on the world 
  y     ;; The y position on the world
  z     ;; The z position on the world, is equal to height of the tile.
  tile  ;; The type of tile, in symbol form. IE :grass, :sea, :mountain
  food  ;; The current food value of this cell
])

(defn find-cell-loc
  "The formula for getting the 'location' of a cell while knowing the cells x and y is:
   world-size   = 1 = max [x | cell-from-world]
   location(cell)       = cell(y) * world-size + cell(x)
   location(cell(1, 1)) = 1 * 2 + 1 = 4
   
   Note that the world has to be a perfect square."
  [x y world-size]
  (+ x (* world-size y)))  

(defn find-cell
  "Returns the cell"
  [x y actual-world world-size]
  (nth actual-world (find-cell-loc x y world-size)))

(defn find-cells
  "Find all cells in the actual world from the coll, which is a collection of [{:x, :y}]"
  [actual-world coll world-size]
  (map #(find-cell (:x %) (:y %) actual-world world-size) coll))

(defn cells-with-food
  "Gets all the Cells with food from the world."
  [world]
  (filter #(> (:food %1) 0) world))

(defn surrounding-cells-by-mask
  "Gets the surrounding cell's of a specific cell. What the surroundings cells are 
   is determinded by the mask argument. The cells returned are within the bounds of the world.
   Returns [{:x, :y} ...] of all cells within the mask and the bounds."
  [x, y, mask, world-size]
  (filter
    #(and (>= (:x %) 0) (< (:x %) world-size) (>= (:y %) 0) (< (:y %) world-size))
	(map 
		#(let [dx (first %)
			     dy (last %)]
			  {:x (+ x dx) :y (+ y dy)}) mask)))

(defn generate-unknown-world
  "Generates an uknown square world the size is determined by the first argument"
  [size]
  (vec (map #(Cell. (mod % size) (int (/ % size)) 0 :unknown 0) (range (* size size)))))
