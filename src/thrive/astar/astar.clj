(ns thrive.astar.astar
  (:use [thrive.human])
  (:use [thrive.cell])
  (:import (thrive.cell Cell)))


(defn manhattan-distance 
  "http://en.wikipedia.org/wiki/Taxicab_geometry"
  [[x1 y1] [x2 y2]]
  (+ (Math/abs ^Integer (- x2 x1)) (Math/abs ^Integer (- y2 y1))))

(defn cost-path
  "Calculate the cost of the path as the name suggets"
  [seq traversable]
  (if (empty? seq)
    0
    (+ ((:tile (first seq)) traversable) (cost-path (rest seq) traversable))))

;(defn euclidian-distance [a b] ; multidimensional
;  (Math/sqrt (reduce + (map #(let [c (- %1 %2)] (* c c)) a b))))

(defn cost 
  "g(x) - cost of getting to that node from starting node. 
    h(x) - cost of getting to the goal node from current node.
    f(x) - g(x)+h(x) "
  [seq end traversable]
  (let [g (cost-path seq traversable) 
        h (manhattan-distance (let [curr (last seq)] [(:x curr) (:y curr)]) end)   
        f (+ g h)]
    [f g h]))

(defn get-path-a*
  "Calcuate a path from the current point to the finish point on the given world"
  [[x1 y1] [x2 y2] movement traversable world world-size]
  (map #() ))


;  (((((((((((((()))))))))))))))
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