(ns thrive.mdp
  (:use [thrive.cell])
  (:import (thrive.cell Cell)))

(defn- max-value-for-cell
  "Returns the highest :value of neighboring cells. What
   the neighbors are are defined by the max-value-mask. 
   If there is no highest value because all neighbors had 
   :value of nil nil is returned."
  [x y max-value-mask world world-size]
  (let [neighbors (surrounding-cells-by-mask x y max-value-mask world-size)
        values    (map #(:value (find-cell (:x %) (:y %) world world-size)) neighbors)]
    ;;(println "("x","y")" " values " values " maxval " maxval)
    (if (every? false? values)
      false
      (let [int-values (filter #(not (or (nil? %) (false? %))) values)]
        (if (empty? int-values)
          nil
          (apply max int-values))))))

(defn- ^Cell assign-value
  "Assigns the :value key for a Cell.
   If the value is already set it doesn do anything.
   If the cell cannot be crossed :value is set to false.
   If a cell is traverable the value is the highest value of a neighboring cell 
   minus the cost of traversing this cell."
  [^Cell c max-value-mask traversable world world-size]
  (if (not (nil? (:value c)))
    c
    (if (= false (traversable (:tile c)))
      (assoc c :value false)
      (let [maxval (max-value-for-cell (:x c) (:y c) max-value-mask world world-size)]
        (if (not (nil? maxval))
          (if (false? maxval)
            (assoc c :value false)
            (assoc c :value (- maxval (traversable (:tile c)))))
          c)))))

(defn value-iteration
  "Returns a world where every cell has a :value based
   on how close it is to the Cell(dx, dy). If a cell is
   not traversable the :value key is false. If it is
   traversable :value is an int. If the number of iterations
   is more that the size of the world, it will assign false to
   the cells that did not have a value. This prevents infinite loops
   caused by an 'island' of cells surrounded by false."
  [dx dy max-value-mask traversable world world-size]
  (let [goal  (assoc (find-cell dx dy world world-size) :value 100)
        world (assoc world (find-cell-loc dx dy world-size) goal)]
    (loop [world world i 0]
      (if (= i (* 2 world-size)) 
        (map #(assoc % :value (if (nil? (:value %)) false (:value %))) world)
        (if (every? #(not (nil? (:value %))) world)
          world
          (recur (map #(assign-value % max-value-mask traversable world world-size) world) (inc i)))))))
                  
(defn plan
  "Takes the start point (x, y) and calculates a route to (dx, dy).
   The route is a vector of vectors: [[0 1] [2 0] [3 5]].
   If no route can be found it will return an emtpy list."
  [x y dx dy movement traversable world world-size]
  (let [max-value-mask (for [[k v] movement :when (not= v [0 0])] v)
        world (value-iteration dx dy max-value-mask traversable world world-size)
        start (find-cell x y world world-size)
        goal  (find-cell dx dy world world-size)]        
    (loop [route [] current start]
      (if (= current goal)
        (map #(vector (:x %) (:y %)) route)
        (let [neighbors-loc (surrounding-cells-by-mask (:x current) (:y current) max-value-mask world-size)
              neighbors     (filter #(not (false? (:value %))) (find-cells world neighbors-loc world-size))]
          (if (empty? neighbors)
            []
            (let [next-cell (reduce #(if (> (:value %1) (:value %2)) %1 %2) neighbors)]  
              (recur (conj route next-cell) next-cell))))))))
