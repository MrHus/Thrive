(ns thrive.astar.astar
  (:use [thrive.cell])
  (:use [clojure.set])
  (:import (thrive.cell Cell)))

(defn manhattan-distance 
  "http://en.wikipedia.org/wiki/Taxicab_geometry
   Calculate a line from a to b with only horizontal and vertical lines"
  [[x1 y1] [x2 y2]]
  (+ (Math/abs ^Integer (- x2 x1)) (Math/abs ^Integer (- y2 y1))))

(defn cost-path
  "Calculate the cost of the path as the name suggets"
  [coll traversable]
  (reduce #(+ %1 ((:tile %2) traversable)) 0 coll))

(defn euclidian-distance [a b]
  "Calculates a straight line from a to b"
  (int (Math/sqrt (reduce + (map #(let [c (- %1 %2)] (* c c)) a b)))))

(defn cost 
  " g(x) - cost of getting to that node from starting node. 
    h(x) - cost of getting to the goal node from current node.
    f(x) - g(x)+h(x) "
  [seq end traversable]
  (let [g (cost-path seq traversable) 
        h (euclidian-distance (let [curr (last seq)] [(:x curr) (:y curr)]) end)   
        f (+ g h)]
    f))

(defn l-contains?
  "A linear search trough a map"
  [value map]
  (if (some #(= value %) map) true (if (and (or (map? value) (vector? value) (list? value)) (some #(= value (val %)) map)) true false)))

(defn get-frontier
  [[x1 y1] [x2 y2] movement traversable world world-size]
  (sort-by :cost (into [] (map #(let [cost (cost [%] [x2 y2] traversable)] {:cost cost :cells [%]}) (find-moveable-cells [x1 y1] movement traversable world world-size)))))

(defn somer
  "just a some filter"
  [item expended]
  (some #(= % item) expended))

(defn get-best-new-cells
  [[x1 y1] [x2 y2] movement traversable expended world world-size]
  (flatten (map :cells (sort-by :cost 
                       (filter #(not (somer (first (:cells %)) expended)) 
                               (get-frontier [x1 y1] [x2 y2] movement traversable world world-size)
                               )))))

(defn calculate-cost
  "Calculates the cost for every item in the list and returns a list with the cost a key"
  [list [x2 y2] traversable]
  (map #(let [cost (cost (:cells %) [x2 y2] traversable)] {:cost cost :cells (:cells %)}) list))

(defn get-path-a*
  "Calculate a path from the current point to the finish point on the given world"
  [[x1 y1] [x2 y2] movement traversable world world-size]
  (loop [frontier (get-frontier [x1 y1] [x2 y2] movement traversable world world-size) 
         expended (conj (into [] (map #(let [cell (first (:cells %))] cell) frontier)) (find-cell x1 y1 world world-size))]
    (let [route (first frontier) 
          active (last (:cells route)) 
          rest-frontier (rest frontier)]
      (if (= [(:x active) (:y active)] [x2 y2])
        (map #(vector (:x %) (:y %)) (:cells route))
        (let [newcells (get-best-new-cells [(:x active) (:y active)] [x2 y2] movement traversable expended world world-size)]
          (if (zero? (count newcells))
            (if (empty? rest-frontier)
              []
              (recur (sort-by :cost rest-frontier) (flatten (conj expended newcells))))
            (recur 
              (sort-by :cost (flatten  (conj rest-frontier 
                                             (map 
                                               (fn [cell] 
                                                 {:cost (cost [cell] [x2 y2] traversable) :cells (conj (:cells route) cell)}
                                                 ) newcells))))
              (flatten (conj expended newcells))
              )))))))