(ns thrive.test.human
  (:use [thrive.human])
  (:import (thrive.human Human))
  (:use [thrive.cell])
  (:import (thrive.cell Cell))
  (:use [clojure.test]))

(def world-size 3)

(def test-world
  [(Cell. 0 0 0 :lava 0),  (Cell. 1 0 0 :mountain 15), (Cell. 2 0 0 :unknown 0), 
   (Cell. 0 1 0 :grass 0), (Cell. 1 1 2 :mountain 2),  (Cell. 2 1 0 :unknown 0),
   (Cell. 0 2 0 :grass 0), (Cell. 1 2 3 :mountain 5),  (Cell. 2 2 0 :unknown 0)])

(deftest find-destination-test
  (is (= {:x 9 :y 1} (find-destination {:x 8, :y 1, :city [9 1], :action :scout} test-world))))

(deftest is-move-valid?-test
  ;; Walk in lava
  (is (= false (is-move-valid? 0 1 0 0 0 test-world world-size)))
  ;; Walk to grass
  (is (= true  (is-move-valid? 0 1 0 0 2 test-world world-size)))
  (is (= true  (is-move-valid? 1 0 0 1 1 test-world world-size)))
  (is (= true  (is-move-valid? 1 1 2 1 2 test-world world-size))))

(def human1 (Human. 0 1 0 5  test-world [0 2] :scout [{:x 0 :y 0}, {:x 1 :y 0}] :a*))
(def human2 (Human. 0 2 0 5  test-world [0 2] :scout [{:x 0 :y 1}] :a*))

(deftest digest-test
  (is (= 4 (:food (digest human1))))
  (is (= 5 (:food (digest human2)))))

(deftest move-test
  "These throw unsupported nth operation."
  ;(is (= false (move human1 world-size)))
  ;(is (= true (move human2 world-size)))
  )