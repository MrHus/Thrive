(ns thrive.test.planner
  (:use [thrive.planner])
  (:use [thrive.cell])
  (:import (thrive.cell Cell))
  (:use [clojure.test]))

(def world-size 3)

(def world
  [(Cell. 0 0 0 :grass 0),  (Cell. 1 0 0 :grass 0),  (Cell. 2 0 0 :grass 0), 
   (Cell. 0 1 0 :grass 0),  (Cell. 1 1 0 :lava 0),   (Cell. 2 1 0 :sea 0),
   (Cell. 0 2 0 :grass 0),   (Cell. 1 2 0 :sea 0),    (Cell. 2 2 0 :sea 0)])

(def movement [[0 0], [0 -1], [0 1], [-1 0], [1 0]])
(def traversable {:grass 1, :mountain 3, :sea 15, :desert 2, :lava false})

(deftest get-plan-test
  (is (=  [] (get-plan :mdp 0 0 0 0 movement traversable world world-size)))
  (is (=  [[0 1] [0 0] [1 0] [2 0] [2 1]] (get-plan :mdp 0 2 2 1 movement traversable world world-size)))
  (is (=  [[1 0] [2 0] [2 1] [2 2]] (get-plan :mdp 0 0 2 2 movement traversable world world-size)))
  (is (= [[0 0]] (get-plan :a* 0 0 0 0 movement traversable world world-size)))
  ;(is (= [[1 0] [2 0] [2 1] [2 2]] (get-plan :astar 0 0 2 2 movement
  ;traversable world world-size)))
  )