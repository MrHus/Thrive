(ns thrive.test.cell
  (:use [thrive.cell])
  (:import (thrive.cell Cell))
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

(deftest cells-with-food-test
  (is (= 3 (count (cells-with-food test-world))))
  (is (= (Cell. 1 0 0 :unknown 15)) (first (cells-with-food test-world))))

(deftest unknown-cells-test
  (is (= 9 (count (unknown-cells test-world))))
  (is (= (every? #(= (:tile %) :unknown) (unknown-cells test-world)))))

(def unknown-world-size-three
  [(Cell. 0 0 0 :unknown 0), (Cell. 1 0 0 :unknown 0), (Cell. 2 0 0 :unknown 0), 
   (Cell. 0 1 0 :unknown 0), (Cell. 1 1 0 :unknown 0), (Cell. 2 1 0 :unknown 0),
   (Cell. 0 2 0 :unknown 0), (Cell. 1 2 0 :unknown 0), (Cell. 2 2 0 :unknown 0)])
	 
(def unknown-world-size-two
  [(Cell. 0 0 0 :unknown 0), (Cell. 1 0 0 :unknown 0), 
	 (Cell. 0 1 0 :unknown 0), (Cell. 1 1 0 :unknown 0)])

(def mask [[0 0] [-1 0] [1 0] [0 -1] [0 1]])

(deftest surrounding-cells-by-mask-test
  ;; The x and y are not near edges 
 	(is (= [{:x 1 :y 1}, {:x 0 :y 1} {:x 2 :y 1}, {:x 1 :y 0}, {:x 1 :y 2}] (surrounding-cells-by-mask 1 1 mask world-size)))
 	;; The x and y are near edges
 	(is (= [{:x 0 :y 0}, {:x 1 :y 0}, {:x 0 :y 1}] (surrounding-cells-by-mask 0 0 mask world-size)))
 	(is (= [{:x 2 :y 2}, {:x 1 :y 2}, {:x 2 :y 1}] (surrounding-cells-by-mask 2 2 mask world-size))))

(def movement [[0, 0], [-1, 0], [1, 0], [0, -1], [0, 1]])
(def traversable {:grass 1, :forest 2, :mountain 3, :desert 2 :sea 5, :unknown 25, :lava false})

(def test-world-known
  [(Cell. 0 0 0 :lava 0),  (Cell. 1 0 0 :mountain 0),  (Cell. 2 0 0 :grass 0), 
   (Cell. 0 1 0 :grass 0), (Cell. 1 1 2 :mountain 0),  (Cell. 2 1 0 :grass 0),
   (Cell. 0 2 0 :grass 0), (Cell. 1 2 3 :mountain 0),  (Cell. 2 2 0 :grass 0)])
 
(deftest find-moveable-cells-test
  (is (= [(Cell. 0 2 0 :grass 0), (Cell. 1 2 3 :mountain 0), (Cell. 0 1 0 :grass 0)] (find-moveable-cells [0 2] movement traversable test-world-known world-size)))
  (is (= [(Cell. 1 1 2 :mountain 0) (Cell. 0 1 0 :grass 0) (Cell. 2 1 0 :grass 0) (Cell. 1 0 0 :mountain 0) (Cell. 1 2 3 :mountain 0)] (find-moveable-cells [1 1] movement traversable test-world-known world-size)))
  (is (= [(Cell. 1 0 0 :mountain 0) (Cell. 2 0 0 :grass 0) (Cell. 1 1 2 :mountain 0)] (find-moveable-cells [1 0] movement traversable test-world-known world-size)))) 
 
(deftest generate-unkown-world-test
  ;; count is correct should be x * x 
  (is (= 9 (count (generate-unknown-world 3))))
  (is (= 16 (count (generate-unknown-world 4))))
  (is (= 25 (count (generate-unknown-world 5))))	
  
  (is (= unknown-world-size-three (generate-unknown-world 3)))
  (is (= unknown-world-size-two   (generate-unknown-world 2))))
  
(def cell-time-10 (assoc (Cell. 0 2 0 :grass 0) :time 10)) 
(def cell-time-no (Cell. 0 2 0 :grass 0)) 
  
(deftest get-time-test
  (is (= 10 (get-time cell-time-10)))
  (is (= 0  (get-time cell-time-no))))

(deftest get-cell-with-most-time-test
  (is (= cell-time-10 (get-cell-with-most-time cell-time-no cell-time-10)))
  (is (= cell-time-10 (get-cell-with-most-time cell-time-10 cell-time-no))))
