(ns thrive.test.mdp
  (:use [thrive.mdp])
  (:use [clojure.test])
  (:import (thrive.cell Cell)))

(def world-size 3)

(def traversable {:city 1, :grass 1, :mountain 3, :sea 15, :lava false})

(def test-world
  [(Cell. 0 0 0 :grass 0),  (Cell. 1 0 0 :grass 0),  (Cell. 2 0 0 :grass 0), 
   (Cell. 0 1 0 :grass 0),  (Cell. 1 1 0 :lava 0),   (Cell. 2 1 0 :sea 0),
   (Cell. 0 2 0 :city 0),   (Cell. 1 2 0 :sea 0),    (Cell. 2 2 0 :sea 0)])

(def test-world-value-iterated
  [(assoc (Cell. 0 0 0 :grass 0) :value 98), (assoc (Cell. 1 0 0 :grass 0) :value 99),   (assoc (Cell. 2 0 0 :grass 0) :value 100), 
   (assoc (Cell. 0 1 0 :grass 0) :value 97), (assoc (Cell. 1 1 0 :lava 0), :value false) (assoc (Cell. 2 1 0 :sea 0)   :value 85),
   (assoc (Cell. 0 2 0 :city 0)  :value 96), (assoc (Cell. 1 2 0 :sea 0),  :value 55)    (assoc (Cell. 2 2 0 :sea 0)   :value 70)]) 
  
(deftest value-iteration-test
  (is (= test-world-value-iterated (value-iteration 2 0 test-world world-size))))