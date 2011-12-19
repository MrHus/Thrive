(ns thrive.test.human
	(:use [thrive.human])
	(:use [clojure.test]))
	
(deftest neighbors-test
	(is [{:x 1 :y 1}, {:x 0 :y 1} {:x 2 :y 1}, {:x 1 :y 0}, {:x 1 :y 2}] (neighbors 1 1)))
		