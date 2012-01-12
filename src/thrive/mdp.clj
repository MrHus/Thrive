(ns thrive.mdp
  (:use [thrive.human :only (traversable, movement)])
  (:use [thrive.cell])
  (:import (thrive.cell Cell)))

;; Define where the neighbors are. (left, right, up, down)
(def max-value-mask [[-1 0] [1 0] [0 -1] [0 1]])

(defn- max-value-for-cell
  "Returns the highest :value of neighboring cells.
   If there is no highest value because all neighbors had 
   :value of nil nil is returned."
  [x y world world-size]
  (let [neighbors (surrounding-cells-by-mask x y max-value-mask world-size)
        values    (map #(:value (find-cell (:x %) (:y %) world world-size)) neighbors)]
    ;;(println "("x","y")" " values " values " maxval " maxval)
    (if (every? false? values)
      false
      (let [maxval (apply max (map #(if (or (nil? %) (false? %)) -9999 %) values))]
        (if (= maxval -9999)
          nil
          maxval)))))

(defn- ^Cell assign-value
  "Assigns the :value key for a Cell.
   If the value is already set it doesn do anything.
   If the cell cannot be crossed :value is set to false.
   If a cell is traverable the value is the highest value of a neighboring cell 
   minus the cost of traversing this cell."
  [^Cell c world world-size]
  (if (not (nil? (:value c)))
    c
    (if (= false (traversable (:tile c)))
      (assoc c :value false)
      (let [maxval (max-value-for-cell (:x c) (:y c) world world-size)]
        (if (not (nil? maxval))
          (if (false? maxval)
            (assoc c :value false)
            (assoc c :value (- maxval (traversable (:tile c)))))
          c)))))

(defn value-iteration
  "Returns a world where every cell has a :value based
   on how close it is to the Cell(dx, dy). If a cell is
   not traversable the :value key is false. If it is
   traversable :value is an int."
  [dx dy world world-size]
  (let [goal (assoc (find-cell dx dy world world-size) :value 100)
        world (assoc world (find-cell-loc dx dy world-size) goal)]
    (loop [world world]
      (if (every? #(not (nil? (:value %))) world)
        world
        (recur (map #(assign-value % world world-size) world))))))
  