(ns thrive.test.astar
  (:use [thrive.astar.astar])
  (:use [clojure.test])
  (:use [thrive.cell])
  (:import (thrive.cell Cell)))

(def movement {:stay [0,0], :left [1, 0], :right [1, 0], :up [0, -1], :down [0, 1]})
(def traversable {:city 1, :grass 1, :sand 2, :mountain 2, :sea 15, :lava false})

  
(deftest test-manhattan-distance
  (is (= (manhattan-distance [0, 0] [0, 5]) 5 ))
  (is (= (manhattan-distance [0, 0] [5, 1]) 6 ))
  (is (= (manhattan-distance [0, 0] [1, 5]) 6 ))
  (is (= (manhattan-distance [5, 0] [0, 0]) 5 ))
  
  (is (= (manhattan-distance [0, 5] [0, 0]) 5 ))
  (is (= (manhattan-distance [5, 0] [0, 0]) 5 ))
  (is (= (manhattan-distance [5, 1] [0, 0]) 6 ))
  (is (= (manhattan-distance [5, 1] [0, 0]) 6 ))
  
  (is (= (manhattan-distance [0, 0] [3, 3]) 6 ))
  (is (= (manhattan-distance [3, 3] [0, 0]) 6 ))
  (is (= (manhattan-distance [2, 2] [4, 4]) 4 ))
  (is (= (manhattan-distance [4, 4] [2, 2]) 4 ))
  )

(deftest test-cost-path
  (is (= (cost-path [(Cell. 0 0 0 :grass 0) (Cell. 0 0 0 :grass 0) (Cell. 0 0 0 :grass 0)] traversable) 3))
  (is (= (cost-path [(Cell. 0 0 0 :grass 0) (Cell. 0 0 0 :sand 0)] traversable) 3))
  )

(deftest test-cost
  (is (= (cost [(Cell. 0 0 0 :grass 0)] [1 0] traversable ) [2 1 1]))
  (is (= (cost [(Cell. 0 0 0 :grass 0)] [8 0] traversable ) [9 1 8]))
  (is (= (cost [(Cell. 0 0 0 :grass 0)] [9 0] traversable ) [10 1 9]))
  (is (= (cost [(Cell. 0 0 0 :grass 0) (Cell. 1 0 0 :grass 0)] [9 0] traversable ) [10 2 8]))
  )

(deftest test-a*
  ;(is (= (get-path-a* [1 6] [1 1] traversable simple-world) [[1 5], [1, 4], [1, 3], [1,2] [1, 1]]))
  ;(is (= (get-path-a* [1 1] [1 6] traversable simple-world) [[1 2], [1, 2], [1, 3], [1,4] [1, 6]]))

  ;(is (= (get-path-a* [2 1] [2 6] traversable round-world) [[1, 1] [1, 2] [1, 3] [1, 4] [1, 5] [1, 6] [2, 6]]))
  ;(is (= (get-path-a* [2 6] [2 1] traversable round-world) [[1, 6] [1, 5] [1, 4] [1, 3] [1, 2] [1, 1] [2, 1]]))
  
  ;(is (= (get-path-a* [1 1] [1 2] traversable puzzle-world) []))
  ;(is (= (get-path-a* [1 1] [1 2] traversable puzzle-world) []))
  ) 