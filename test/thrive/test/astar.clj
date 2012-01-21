(ns thrive.test.astar
  (:use [thrive.astar.astar])
  (:use [clojure.test])
  (:use [thrive.cell])
  (:import (thrive.cell Cell)))

(def movement {:stay [0,0], :left [1, 0], :right [1, 0], :up [0, -1], :down [0, 1]})
(def traversable {:grass 1, :sand 2, :mountain 2, :sea 15, :lava false})

(def simple-world-size 3)
(def simple-world
  [(Cell. 0 0 0 :lava 0),  (Cell. 1 0 0 :lava 0),  (Cell. 2 0 0 :lava 0), 
   (Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :lava 0),
   (Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :lava 0),
   (Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :lava 0),
   (Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :lava 0),
   (Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :lava 0),
   (Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :lava 0),
   (Cell. 0 2 0 :lava 0),  (Cell. 1 2 0 :lava 0),  (Cell. 2 2 0 :lava 0)])

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
  (is (= 3 (cost-path [(Cell. 0 0 0 :grass 0) (Cell. 0 0 0 :grass 0) (Cell. 0 0 0 :grass 0)] traversable)))
  (is (= 3 (cost-path [(Cell. 0 0 0 :grass 0) (Cell. 0 0 0 :sand 0)] traversable))))

(deftest test-cost
  (is (= [2 1 1]  (cost [(Cell. 0 0 0 :grass 0)] [1 0] traversable)))
  (is (= [10 2 8] (cost [(Cell. 0 0 0 :grass 0) (Cell. 1 0 0 :grass 0)] [9 0] traversable)))
  (is (= [11 3 8] (cost [(Cell. 0 0 0 :grass 0) (Cell. 1 0 0 :sand 0)] [9 0] traversable))))

(comment
(deftest test-a*
  (is (= [[1 5], [1 4], [1 3], [1 2] [1 1]] (get-path-a* [1 6] [1 1] traversable movement simple-world simple-world-size)))
  (is (= [[1 2], [1 2], [1 3], [1 4] [1 6]] (get-path-a* [1 1] [1 6] traversable simple-world)))

  (is (= [[1, 1] [1, 2] [1, 3] [1, 4] [1, 5] [1, 6] [2, 6]] (get-path-a* [2 1] [2 6] traversable round-world)))
  (is (= [[1, 6] [1, 5] [1, 4] [1, 3] [1, 2] [1, 1] [2, 1]] (get-path-a* [2 6] [2 1] traversable round-world)))
  
  ;(is (= (get-path-a* [1 1] [1 2] traversable puzzle-world) []))
  ;(is (= (get-path-a* [1 1] [1 2] traversable puzzle-world) []))
  )) 