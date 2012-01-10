(ns thrive.test.city
  (:use [thrive.city])
  (:import (thrive.city City))
  (:use [clojure.test]))

(def c (City. 0 0 0 100 nil)) ;; City with no world

(deftest decrease-food-test
  (is (= 99 (:food (decrease-food c 1)))))
  (is (= 50 (:food (decrease-food c 50))))
  (is (= 0  (:food (decrease-food c 100))))
  (is (= 0  (:food (decrease-food c 101))))
  (is (= 0  (:food (decrease-food c 1444))))
  (is (= (City. 0 0 0 25 nil) (decrease-food c 75)))