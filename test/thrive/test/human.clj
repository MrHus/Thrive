(ns thrive.test.human
  (:use [thrive.human])
  (:use [clojure.test]))

(def world-size 3)

(deftest visibles-test
	;; The x and y are not near edges 
	(is (= [{:x 1 :y 1}, {:x 0 :y 1} {:x 2 :y 1}, {:x 1 :y 0}, {:x 1 :y 2}] (visibles 1 1 world-size)))
	;; The x and y are near edges
	(is (= [{:x 0 :y 0}, {:x 1 :y 0}, {:x 0 :y 1}] (visibles 0 0 world-size)))
	(is (= [{:x 2 :y 2}, {:x 1 :y 2}, {:x 2 :y 1}] (visibles 2 2 world-size))))
  