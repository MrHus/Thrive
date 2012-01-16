(ns thrive.test.multiearray
  (:use [thrive.multiearray.multiearray])
  (:use [clojure.test])
  (:use [thrive.cell])
  (:import (thrive.cell Cell)))

;  0 1 2
;0 L L L
;1 L G L
;2 L G L
;3 L G L
;4 L G L
;5 L G L
;6 L G L
;7 L L L
(def simple-world  [[(Cell. 0 0 0 :lava 0), (Cell. 1 0 0 :lava 0),  (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :lava 0)]
                   [(Cell. 0 2 0 :lava 0),  (Cell. 1 2 0 :grass 0), (Cell. 2 2 0 :lava 0)]
                   [(Cell. 0 3 0 :lava 0),  (Cell. 1 3 0 :grass 0), (Cell. 2 3 0 :lava 0)]
                   [(Cell. 0 4 0 :lava 0),  (Cell. 1 4 0 :grass 0), (Cell. 2 4 0 :lava 0)]
                   [(Cell. 0 5 0 :lava 0),  (Cell. 1 5 0 :grass 0), (Cell. 2 5 0 :lava 0)]
                   [(Cell. 0 6 0 :lava 0),  (Cell. 1 6 0 :grass 0), (Cell. 2 6 0 :lava 0)]
                   [(Cell. 0 7 0 :lava 0),  (Cell. 1 7 0 :lava 0),  (Cell. 2 7 0 :lava 0)]])
;  0 1 2 3 4
;0 L L L L L
;1 L G G G L
;2 L G L G L
;3 L G L S L
;4 L G L G L
;5 L G L G L
;6 L G G G L
;7 L L L L L

;[2, 1] -> [2, 6] = [[1, 1] [1, 2] [1, 3] [1, 4] [1, 5] [1, 6] [2, 6]]
;									  [:left, :down, :down, :down, :down, :down, :right]
;[2, 6] -> [2, 1] = [[1, 6] [1, 5] [1, 4] [1, 3] [1, 2] [1, 1] [2, 1]]
;									  [:left, :up, :up, :up, :up, :up, :right]

(def round-world  [[(Cell. 0 0 0 :lava 0),  (Cell. 1 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 2 0 :lava 0),  (Cell. 1 2 0 :grass 0), (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 3 0 :lava 0),  (Cell. 1 3 0 :grass 0), (Cell. 2 3 0 :lava 0),  (Cell. 2 0 0 :sand 0),  (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 4 0 :lava 0),  (Cell. 1 4 0 :grass 0), (Cell. 2 4 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 5 0 :lava 0),  (Cell. 1 5 0 :grass 0), (Cell. 2 5 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 6 0 :lava 0),  (Cell. 1 6 0 :grass 0), (Cell. 2 6 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 7 0 :lava 0),  (Cell. 1 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 7 0 :lava 0)]])

;  0 1 2 3 4 5 6 7 8
;0 L L L L L L L L L
;1 L G G G G G G G L
;2 L G L G L S L G L
;3 L G L S L G G S L
;4 L G L G L L L G L
;5 L G L G L S S G L
;6 L G G G L G L S L
;7 L L L L L G G G L
;8 L L L L L L L L L
(def puzzle-world  [[(Cell. 0 0 0 :lava 0), (Cell. 1 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 1 0 :lava 0),  (Cell. 1 1 0 :grass 0), (Cell. 2 1 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 2 0 :lava 0),  (Cell. 1 2 0 :grass 0), (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :sand 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 3 0 :lava 0),  (Cell. 1 3 0 :grass 0), (Cell. 2 3 0 :lava 0),  (Cell. 2 0 0 :sand 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :sand 0),  (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 4 0 :lava 0),  (Cell. 1 4 0 :grass 0), (Cell. 2 4 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 5 0 :lava 0),  (Cell. 1 5 0 :grass 0), (Cell. 2 5 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :sand 0),  (Cell. 2 0 0 :sand 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 6 0 :lava 0),  (Cell. 1 6 0 :grass 0), (Cell. 2 6 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :sand 0),  (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 7 0 :lava 0),  (Cell. 1 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :grass 0), (Cell. 2 0 0 :lava 0)]
                   [(Cell. 0 7 0 :lava 0),  (Cell. 1 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 7 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0),  (Cell. 2 0 0 :lava 0)]])


(def map-world {[0 0] (Cell. 0 0 0 :lava 0)})
  
  
(def movement {:stay [0,0], :left [1, 0], :right [1, 0], :up [0, -1], :down [0, 1]})
(def traversable {:city 1, :grass 1, :sand 2, :mountain 2, :sea 15, :lava false})

(deftest test-in?
  (is (= (in? movement [0 1]) true))
  (is (= (in? movement [0 -1]) true))
  (is (= (in? movement [0 2]) false))
  )

(deftest test-is-valid-move?
  (is (= (is-valid-move? [1 6] [1 5] movement traversable simple-world) true))
  (is (= (is-valid-move? [1 5] [1 4] movement traversable simple-world) true))
  (is (= (is-valid-move? [1 5] [2 5] movement traversable simple-world) false))
  )

(deftest test-get-cell
  (is (= (get-cell [0 0] simple-world) (Cell. 0 0 0 :lava 0)))
  (is (= (get-cell [1 2] simple-world) (Cell. 1 2 0 :grass 0)))
  )

(deftest test-is-walkable-cell?
  (is (= (is-walkable-cell? [1 1] traversable simple-world) true))
  (is (= (is-walkable-cell? [1 2] traversable simple-world) true))
  
  (is (= (is-walkable-cell? [-1 0] traversable simple-world) false))
  (is (= (is-walkable-cell? [-1 -1] traversable simple-world) false))
  (is (= (is-walkable-cell? [0 -1] traversable simple-world) false))
  
  
  (is (= (is-walkable-cell? [0 0] traversable simple-world) false))
  (is (= (is-walkable-cell? [2 7] traversable simple-world) false))
  (is (= (is-walkable-cell? [2 3] traversable simple-world) false))
  
  (is (= (is-walkable-cell? [3 0] traversable simple-world) false))
  (is (= (is-walkable-cell? [3 8] traversable simple-world) false))
  (is (= (is-walkable-cell? [0 8] traversable simple-world) false))
  
  (is (= (is-walkable-cell? [3 5] traversable puzzle-world) true))
  (is (= (is-walkable-cell? [5 6] traversable puzzle-world) true))
  (is (= (is-walkable-cell? [6 2] traversable puzzle-world) false))
  
  (is (= (is-walkable-cell? [1 1] traversable puzzle-world) true))
  (is (= (is-walkable-cell? [9 9] traversable puzzle-world) false))
  (is (= (is-walkable-cell? [9 1] traversable puzzle-world) false))
  (is (= (is-walkable-cell? [1 9] traversable puzzle-world) false))
  )




  