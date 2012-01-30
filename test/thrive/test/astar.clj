(ns thrive.test.astar
  (:use [thrive.astar.astar])
  (:use [clojure.test])
  (:use [thrive.cell])
  (:import (thrive.cell Cell)))

;(def movement {:stay [0,0], :left [-1, 0], :right [1, 0], :up [0, -1], :down [0, 1]})
(def movement {:left [-1, 0], :right [1, 0], :up [0, -1], :down [0, 1]})
(def traversable {:grass 1, :sand 2, :mountain 2, :sea 15, :lava false})

(def simple-world-size 8)
(def simple-world
  [(Cell. 0 0 0 :lava 0),  (Cell. 1 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 3 0 0 :lava 0),  (Cell. 4 0 0 :lava 0),  (Cell. 5 0 0 :lava 0),  (Cell. 6 0 0 :lava 0),  (Cell. 7 0 0 :lava 0), 
   (Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :lava 0),  (Cell. 3 1 0 :lava 0),  (Cell. 4 1 0 :lava 0),  (Cell. 5 1 0 :lava 0),  (Cell. 6 1 0 :lava 0),  (Cell. 7 1 0 :lava 0),
   (Cell. 0 2 0 :lava 0),  (Cell. 1 2 0 :grass 0), (Cell. 2 2 0 :lava 0),  (Cell. 3 2 0 :lava 0),  (Cell. 4 2 0 :lava 0),  (Cell. 5 2 0 :lava 0),  (Cell. 6 2 0 :lava 0),  (Cell. 7 2 0 :lava 0),
   (Cell. 0 3 0 :lava 0),  (Cell. 1 3 0 :grass 0), (Cell. 2 3 0 :lava 0),  (Cell. 3 3 0 :lava 0),  (Cell. 4 3 0 :lava 0),  (Cell. 5 3 0 :lava 0),  (Cell. 6 3 0 :lava 0),  (Cell. 7 3 0 :lava 0),
   (Cell. 0 4 0 :lava 0),  (Cell. 1 4 0 :grass 0), (Cell. 2 4 0 :lava 0),  (Cell. 3 4 0 :lava 0),  (Cell. 4 4 0 :lava 0),  (Cell. 5 4 0 :lava 0),  (Cell. 6 4 0 :lava 0),  (Cell. 7 4 0 :lava 0),
   (Cell. 0 5 0 :lava 0),  (Cell. 1 5 0 :grass 0), (Cell. 2 5 0 :lava 0),  (Cell. 3 5 0 :lava 0),  (Cell. 4 5 0 :lava 0),  (Cell. 5 5 0 :lava 0),  (Cell. 6 5 0 :lava 0),  (Cell. 7 5 0 :lava 0),
   (Cell. 0 6 0 :lava 0),  (Cell. 1 6 0 :grass 0), (Cell. 2 6 0 :lava 0),  (Cell. 3 6 0 :lava 0),  (Cell. 4 6 0 :lava 0),  (Cell. 5 6 0 :lava 0),  (Cell. 6 6 0 :lava 0),  (Cell. 7 6 0 :lava 0),
   (Cell. 0 7 0 :lava 0),  (Cell. 1 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 3 7 0 :lava 0),  (Cell. 4 7 0 :lava 0),  (Cell. 5 7 0 :lava 0),  (Cell. 6 7 0 :lava 0),  (Cell. 7 7 0 :lava 0)])

(def round-world-size 8)
(def round-world
  [(Cell. 0 0 0 :lava 0),  (Cell. 1 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 3 0 0 :lava 0),  (Cell. 4 0 0 :lava 0),  (Cell. 5 0 0 :lava 0),  (Cell. 6 0 0 :lava 0),  (Cell. 7 0 0 :lava 0), 
   (Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :grass 0),  (Cell. 3 1 0 :grass 0),  (Cell. 4 1 0 :grass 0),  (Cell. 5 1 0 :grass 0),  (Cell. 6 1 0 :grass 0),  (Cell. 7 1 0 :lava 0),
   (Cell. 0 2 0 :lava 0),  (Cell. 1 2 0 :grass 0), (Cell. 2 2 0 :lava 0),  (Cell. 3 2 0 :lava 0),  (Cell. 4 2 0 :lava 0),  (Cell. 5 2 0 :lava 0),  (Cell. 6 2 0 :lava 0),  (Cell. 7 2 0 :lava 0),
   (Cell. 0 3 0 :lava 0),  (Cell. 1 3 0 :grass 0), (Cell. 2 3 0 :lava 0),  (Cell. 3 3 0 :lava 0),  (Cell. 4 3 0 :lava 0),  (Cell. 5 3 0 :lava 0),  (Cell. 6 3 0 :lava 0),  (Cell. 7 3 0 :lava 0),
   (Cell. 0 4 0 :lava 0),  (Cell. 1 4 0 :grass 0), (Cell. 2 4 0 :lava 0),  (Cell. 3 4 0 :lava 0),  (Cell. 4 4 0 :lava 0),  (Cell. 5 4 0 :lava 0),  (Cell. 6 4 0 :lava 0),  (Cell. 7 4 0 :lava 0),
   (Cell. 0 5 0 :lava 0),  (Cell. 1 5 0 :grass 0), (Cell. 2 5 0 :lava 0),  (Cell. 3 5 0 :lava 0),  (Cell. 4 5 0 :lava 0),  (Cell. 5 5 0 :lava 0),  (Cell. 6 5 0 :lava 0),  (Cell. 7 5 0 :lava 0),
   (Cell. 0 6 0 :lava 0),  (Cell. 1 6 0 :grass 0), (Cell. 2 6 0 :grass 0),  (Cell. 3 6 0 :grass 0),  (Cell. 4 6 0 :grass 0),  (Cell. 5 6 0 :grass 0),  (Cell. 6 6 0 :grass 0),  (Cell. 7 6 0 :lava 0),
   (Cell. 0 7 0 :lava 0),  (Cell. 1 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 3 7 0 :lava 0),  (Cell. 4 7 0 :lava 0),  (Cell. 5 7 0 :lava 0),  (Cell. 6 7 0 :lava 0),  (Cell. 7 7 0 :lava 0)])

(deftest test-manhattan-distance
  (is (== (manhattan-distance [0, 0] [0, 5]) 5 ))
  (is (== (manhattan-distance [0, 0] [5, 1]) 6 ))
  (is (== (manhattan-distance [0, 0] [1, 5]) 6 ))
  (is (== (manhattan-distance [5, 0] [0, 0]) 5 ))
  
  (is (== (manhattan-distance [0, 5] [0, 0]) 5 ))
  (is (== (manhattan-distance [5, 0] [0, 0]) 5 ))
  (is (== (manhattan-distance [5, 1] [0, 0]) 6 ))
  (is (== (manhattan-distance [5, 1] [0, 0]) 6 ))
  
  (is (== (manhattan-distance [0, 0] [3, 3]) 6 ))
  (is (== (manhattan-distance [3, 3] [0, 0]) 6 ))
  (is (== (manhattan-distance [2, 2] [4, 4]) 4 ))
  (is (== (manhattan-distance [4, 4] [2, 2]) 4 ))
  )

(deftest test-cost-path
  (is (= 3 (cost-path [(Cell. 0 0 0 :grass 0) (Cell. 0 0 0 :grass 0) (Cell. 0 0 0 :grass 0)] traversable)))
  (is (= 3 (cost-path [(Cell. 0 0 0 :grass 0) (Cell. 0 0 0 :sand 0)] traversable))))

(deftest test-cost
  (is (== 2 (cost [(Cell. 0 0 0 :grass 0)] [1 0] traversable)))
  (is (== 10 (cost [(Cell. 0 0 0 :grass 0) (Cell. 1 0 0 :grass 0)] [9 0] traversable)))
  (is (== 11 (cost [(Cell. 0 0 0 :grass 0) (Cell. 1 0 0 :sand 0)] [9 0] traversable))))

(deftest test-calculate-cost
  (is (= (calculate-cost [{:cells [(Cell. 0 0 0 :grass 0) (Cell. 0 1 0 :grass 0)]}] [0 6] traversable) [{:cost 7 :cells [(Cell. 0 0 0 :grass 0) (Cell. 0 1 0 :grass 0)]}]))
  (is (= (calculate-cost [{:cells [(Cell. 0 1 0 :grass 0)]}] [0 6] traversable) [{:cost 6 :cells [(Cell. 0 1 0 :grass 0)]}]))
  (is (= (calculate-cost [{:cells [(Cell. 2 0 0 :grass 0)]}] [6 0] traversable) [{:cost 5 :cells [(Cell. 2 0 0 :grass 0)]}]))
  )

(deftest test-find-moveable-cells
   (is (= (find-moveable-cells [1 1] movement traversable simple-world simple-world-size) [(Cell. 1 2 0 :grass 0)]))
   (is (= (find-moveable-cells [1 2] movement traversable simple-world simple-world-size) [(Cell. 1 3 0 :grass 0) (Cell. 1 1 0 :grass 0)]))
   (is (= (find-moveable-cells [1 6] movement traversable simple-world simple-world-size) [(Cell. 1 5 0 :grass 0)]))
  )

(deftest test-get-frontier
  (is (= (get-frontier [1 1] [1 6] movement traversable simple-world simple-world-size) [{:cost 5 :cells [(Cell. 1 2 0 :grass 0)]}]))
  (is (= (get-frontier [1 6] [1 1] movement traversable simple-world simple-world-size) [{:cost 5 :cells [(Cell. 1 5 0 :grass 0)]}]))
  )

(deftest test-a*
  (is (= (get-path-a* [1 1] [1 6] movement traversable simple-world simple-world-size) [(Cell. 1 2 0 :grass 0) (Cell. 1 3 0 :grass 0) (Cell. 1 4 0 :grass 0) (Cell. 1 5 0 :grass 0) (Cell. 1 6 0 :grass 0)]))
  (is (= (get-path-a* [1 6] [1 1] movement traversable simple-world simple-world-size) [(Cell. 1 5 0 :grass 0) (Cell. 1 4 0 :grass 0) (Cell. 1 3 0 :grass 0) (Cell. 1 2 0 :grass 0) (Cell. 1 1 0 :grass 0)]))
  
  
  (is (= (get-path-a* [6 1] [6 6] movement traversable round-world round-world-size) [(Cell. 5 1 0 :grass 0) (Cell. 4 1 0 :grass 0) (Cell. 3 1 0 :grass 0) (Cell. 2 1 0 :grass 0) (Cell. 1 1 0 :grass 0) (Cell. 1 2 0 :grass 0) (Cell. 1 3 0 :grass 0) (Cell. 1 4 0 :grass 0) (Cell. 1 5 0 :grass 0) (Cell. 1 6 0 :grass 0) (Cell. 2 6 0 :grass 0) (Cell. 3 6 0 :grass 0) (Cell. 4 6 0 :grass 0) (Cell. 5 6 0 :grass 0) (Cell. 6 6 0 :grass 0)]))
  (is (= (get-path-a* [6 6] [6 1] movement traversable round-world round-world-size) [(Cell. 5 6 0 :grass 0) (Cell. 4 6 0 :grass 0) (Cell. 3 6 0 :grass 0) (Cell. 2 6 0 :grass 0) (Cell. 1 6 0 :grass 0) (Cell. 1 5 0 :grass 0) (Cell. 1 4 0 :grass 0) (Cell. 1 3 0 :grass 0) (Cell. 1 2 0 :grass 0) (Cell. 1 1 0 :grass 0) (Cell. 2 1 0 :grass 0) (Cell. 3 1 0 :grass 0) (Cell. 4 1 0 :grass 0) (Cell. 5 1 0 :grass 0) (Cell. 6 1 0 :grass 0)]))

  ;(is (= [[1 5], [1 4], [1 3], [1 2] [1 1]] (get-path-a* [1 6] [1 1] traversable movement simple-world simple-world-size)))
  ;(is (= [[1 2], [1 2], [1 3], [1 4] [1 6]] (get-path-a* [1 1] [1 6] traversable simple-world)))
  
  ;(is (= [[1, 1] [1, 2] [1, 3] [1, 4] [1, 5] [1, 6] [2, 6]] (get-path-a* [2 1] [2 6] traversable round-world)))
  ;(is (= [[1, 6] [1, 5] [1, 4] [1, 3] [1, 2] [1, 1] [2, 1]] (get-path-a* [2 6] [2 1] traversable round-world)))
  
  ;(is (= (get-path-a* [1 1] [1 2] traversable puzzle-world) []))
  ;(is (= (get-path-a* [1 1] [1 2] traversable puzzle-world) []))
  )
