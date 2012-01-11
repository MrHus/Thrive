(ns thrive.test.core
  (:use [thrive.core])
  (:import (thrive.core Cell))
  (:use [clojure.test]))

(def unknown-world-size-three
  [(Cell. 0 0 0 :unknown 0), (Cell. 1 0 0 :unknown 0), (Cell. 2 0 0 :unknown 0), 
   (Cell. 0 1 0 :unknown 0), (Cell. 1 1 0 :unknown 0), (Cell. 2 1 0 :unknown 0),
   (Cell. 0 2 0 :unknown 0), (Cell. 1 2 0 :unknown 0), (Cell. 2 2 0 :unknown 0)])
	 
(def unknown-world-size-two
  [(Cell. 0 0 0 :unknown 0), (Cell. 1 0 0 :unknown 0), 
	 (Cell. 0 1 0 :unknown 0), (Cell. 1 1 0 :unknown 0)])
	 
(deftest generate-unkown-world-test
  ;; count is correct should be x * x 
  (is (=  9 (count (generate-unkown-world 3))))
  (is (= 16 (count (generate-unkown-world 4))))
	(is (= 25 (count (generate-unkown-world 5))))	
  
	(is (= unknown-world-size-three (generate-unkown-world 3)))
	(is (= unknown-world-size-two   (generate-unkown-world 2))))
