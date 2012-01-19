(ns thrive.test.human
  (:use [thrive.human])
  (:use [thrive.cell])
  (:import (thrive.cell Cell))
  (:use [clojure.test]))

(def world-size 3)

(def test-world
  [(Cell. 0 0 0 :lava 0),  (Cell. 1 0 0 :mountain 15), (Cell. 2 0 0 :unknown 0), 
   (Cell. 0 1 0 :grass 0), (Cell. 1 1 2 :mountain 2),  (Cell. 2 1 0 :unknown 0),
   (Cell. 0 2 0 :grass 0), (Cell. 1 2 3 :mountain 5),  (Cell. 2 2 0 :unknown 0)])

(deftest is-move-valid?-test
  ;; Walk in lava
  (is (= false (is-move-valid? 0 1 0 0 0 test-world world-size)))
  ;; Walk to grass
  (is (= true  (is-move-valid? 0 1 0 0 2 test-world world-size)))
  (is (= true  (is-move-valid? 1 0 0 1 1 test-world world-size)))
  (is (= true  (is-move-valid? 1 1 2 1 2 test-world world-size))))
  