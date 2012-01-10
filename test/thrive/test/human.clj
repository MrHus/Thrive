(ns thrive.test.human
	(:use [thrive.human])
	(:use [thrive.core])
    (:import (thrive.core Cell))
	(:use [clojure.test]))

(def world-size 3)

(def test-world
  [(Cell. 0 0 0 :unknown 0), (Cell. 1 0 0 :unknown 15), (Cell. 2 0 0 :unknown 0), 
   (Cell. 0 1 0 :unknown 0), (Cell. 1 1 0 :unknown 2), (Cell. 2 1 0 :unknown 0),
   (Cell. 0 2 0 :unknown 0), (Cell. 1 2 0 :unknown 5), (Cell. 2 2 0 :unknown 0)])

(deftest find-cell-loc-test
  (is (= 0 (find-cell-loc 0 0 world-size)))
  (is (= 1 (find-cell-loc 1 0 world-size)))
  (is (= 2 (find-cell-loc 2 0 world-size)))
  (is (= 4 (find-cell-loc 1 1 world-size)))
  (is (= 8 (find-cell-loc 2 2 world-size))))

(deftest find-cell-test
  (is (= (Cell. 0 1 0 :unknown 0) (find-cell 0 1 test-world world-size)))
  (is (= (Cell. 1 0 0 :unknown 15) (find-cell 1 0 test-world world-size)))
  (is (= (Cell. 2 2 0 :unknown 0) (find-cell 2 2 test-world world-size))))

(deftest find-cells-test
  (is (= 4 (count (find-cells test-world [{:x 0 :y 1}, {:x 2 :y 1} {:x 0 :y 2}, {:x 1 :y 2}] world-size))))
  (is (= [(Cell. 0 1 0 :unknown 0), (Cell. 2 1 0 :unknown 0)] (find-cells test-world [{:x 0 :y 1}, {:x 2 :y 1}] world-size))))

(deftest visibles-test
	;; The x and y are not near edges 
	(is (= [{:x 1 :y 1}, {:x 0 :y 1} {:x 2 :y 1}, {:x 1 :y 0}, {:x 1 :y 2}] (visibles 1 1 world-size)))
	;; The x and y are near edges
	(is (= [{:x 0 :y 0}, {:x 1 :y 0}, {:x 0 :y 1}] (visibles 0 0 world-size)))
	(is (= [{:x 2 :y 2}, {:x 1 :y 2}, {:x 2 :y 1}] (visibles 2 2 world-size))))	
  
(deftest cells-with-food-test
  (is (= 3 (count (cells-with-food test-world))))
  (is (= (Cell. 1 0 0 :unknown 15)) (first (cells-with-food test-world))))   
