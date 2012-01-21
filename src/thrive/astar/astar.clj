(ns thrive.astar.astar
  (:use [thrive.human])
  (:use [thrive.cell])
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
  (Math/sqrt (reduce + (map #(let [c (- %1 %2)] (* c c)) a b))))

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

(defn get-path-a*
  "Calculate a path from the current point to the finish point on the given world"
  [[x1 y1] [x2 y2] movement traversable world world-size]  
  (loop [frontier (find-cell x1 y2 world world-size) expended []]
    (let [cells 
          (find-cells world 
                      (surrounding-cells-by-mask 
                        (:x (first frontier)) (:y (first frontier)) 
                        (map #(val %) movement) 
                        world-size) 
                      world-size)]
      (if (= [1 2] [x2 y2])
        frontier
        false
        ;(recur (sort (map #((cost (find-cell x1 y2 world world-size) movement)))))
        ))))

(defn calculates-cost
  "Calculates the cost for every item in the list and returns a list with the cost a key"
  [list [x2 y2] traversable]
  (map #(let [cost (int (cost % [x2 y2] traversable))] {(keyword (str cost)) %} )  list))
;(map #(let [cost (int (cost % [0 6] traversable))] {(keyword (str cost)) %} ) [[{:x 0 :y 0 :tile :grass} {:x 1 :y 0 :tile :grass}] [{:x 1 :y 0 :tile :grass}] [{:x 7 :y 0 :tile :grass}]])

;  (((((((((((((()))))))))))))))
;surrounding-cells-by-mask 1 1 mask world-size
; surrounding-cells-by-mask
;
; (loop [seq current ]
;  (if (== current goal)
;    (seq)
;   (doseq [m movement] 
;    (cost 
;     (find-cell (+ (:x (val m)) x1) (+ (:y (val m)) y1) world world-size)
;    [x2 y2] traversable)
;   )
; (recur [])))))