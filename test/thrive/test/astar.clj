(ns thrive.test.astar
  (:use [thrive.pathfinding.astar])
  (:use [clojure.test])
  (:use [thrive.cell])
  (:import (thrive.cell Cell)))

;L L L
;L G L
;L G L
;L G L
;L G L
;L G L
;L G L
;L L L
(def simple-world  [[(Cell. 0 0 0 :lava 0), (Cell. 1 0 0 :lava 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 1 0 :lava 0), (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :lava 0)]
                   [(Cell. 0 2 0 :lava 0), (Cell. 1 2 0 :grass 0), (Cell. 2 2 0 :lava 0)]
                   [(Cell. 0 3 0 :lava 0), (Cell. 1 3 0 :grass 0), (Cell. 2 3 0 :lava 0)]
                   [(Cell. 0 4 0 :lava 0), (Cell. 1 4 0 :grass 0), (Cell. 2 4 0 :lava 0)]
                   [(Cell. 0 5 0 :lava 0), (Cell. 1 5 0 :grass 0), (Cell. 2 5 0 :lava 0)]
                   [(Cell. 0 6 0 :lava 0), (Cell. 1 6 0 :grass 0), (Cell. 2 6 0 :lava 0)]
                   [(Cell. 0 7 0 :lava 0), (Cell. 1 7 0 :lava 0), (Cell. 2 7 0 :lava 0)]])

;L L L L L
;L G G G L
;L G L G L
;L G L S L
;L G L G L
;L G L G L
;L G G G L
;L L L L L
(def round-world  [[(Cell. 0 0 0 :lava 0), (Cell. 1 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 2 0 :lava 0),  (Cell. 1 2 0 :grass 0), (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 3 0 :lava 0),  (Cell. 1 3 0 :grass 0), (Cell. 2 3 0 :lava 0),  (Cell. 2 0 0 :sand 0),  (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 4 0 :lava 0),  (Cell. 1 4 0 :grass 0), (Cell. 2 4 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 5 0 :lava 0),  (Cell. 1 5 0 :grass 0), (Cell. 2 5 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 6 0 :lava 0),  (Cell. 1 6 0 :grass 0), (Cell. 2 6 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 7 0 :lava 0),  (Cell. 1 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 7 0 :lava 0)]])

;L L L L L
;L G G G L
;L G L G L
;L G L S L
;L G L G L
;L G L G L
;L G G G L
;L L L L L
(def puzzle-world  [[(Cell. 0 0 0 :lava 0), (Cell. 1 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 2 0 :lava 0),  (Cell. 1 2 0 :grass 0), (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 3 0 :lava 0),  (Cell. 1 3 0 :grass 0), (Cell. 2 3 0 :lava 0),  (Cell. 2 0 0 :sand 0),  (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 4 0 :lava 0),  (Cell. 1 4 0 :grass 0), (Cell. 2 4 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 5 0 :lava 0),  (Cell. 1 5 0 :grass 0), (Cell. 2 5 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 6 0 :lava 0),  (Cell. 1 6 0 :grass 0), (Cell. 2 6 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 7 0 :lava 0),  (Cell. 1 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 7 0 :lava 0)]])

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
  (is (= (manhattan-distance [4, 4] [2, 2]) 4 )))

(deftest test-a*
  (is (= (get-path-a* [1 6] [1 1] traversable simple-world) [[1 5], [1, 4], [1, 3], [1,2] [1, 1]]))
  (is (= (get-path-a* [1 1] [1 6] traversable simple-world) [[1 2], [1, 2], [1, 3], [1,4] [1, 6]]))
  
  (is (= (get-path-a* [1 1] [1 6] traversable round-world) [[1 2], [1, 2], [1, 3], [1,4] [1, 6]]))
  )
  
  
  
  