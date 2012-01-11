(ns thrive.test.human
  (:use [thrive.human])
  (:use [thrive.cell])
  (:import (thrive.cell Cell))
  (:use [clojure.test]))

(def world-size 3)

(def test-world
  [(Cell. 0 0 0 :lava 0),  (Cell. 1 0 0  :mountain 15), (Cell. 2 0 0 :unknown 0), 
   (Cell. 0 1 0 :grass 0), (Cell. 1 1 2 :mountain 2), (Cell. 2 1 0 :unknown 0),
   (Cell. 0 2 0 :grass 0), (Cell. 1 2 3 :mountain 5), (Cell. 2 2 0 :unknown 0)])

(deftest visibles-test
	;; The x and y are not near edges 
	(is (= [{:x 1 :y 1}, {:x 0 :y 1} {:x 2 :y 1}, {:x 1 :y 0}, {:x 1 :y 2}] (visibles 1 1 world-size)))
	;; The x and y are near edges
	(is (= [{:x 0 :y 0}, {:x 1 :y 0}, {:x 0 :y 1}] (visibles 0 0 world-size)))
	(is (= [{:x 2 :y 2}, {:x 1 :y 2}, {:x 2 :y 1}] (visibles 2 2 world-size))))

(deftest is-move-valid?-test
  ;; Walk in lava
  (is (= false (is-move-valid? 0 1 0 0 0 test-world world-size)))
  ;; Walk to grass
  (is (= true  (is-move-valid? 0 1 0 0 2 test-world world-size)))  
  ;; Walk up mountain which is to high
  (is (= false (is-move-valid? 1 0 0 1 1 test-world world-size)))
  ;; Walk up mountain which is possible
  (is (= false (is-move-valid? 1 1 2 1 2 test-world world-size))))
  