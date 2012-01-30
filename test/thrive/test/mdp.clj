(ns thrive.test.mdp
  (:use [thrive.mdp])
  (:use [clojure.test])
  (:import (thrive.cell Cell)))

(def world-size 3)

(def movement {:stay [0 0], :left [0 -1], :right [0 1], :up [-1 0], :down [1 0]})
(def movement-mask [[0 -1] [0 1] [-1 0] [1 0]])
(def traversable {:grass 1, :mountain 3, :sea 15, :desert 2, :unknown 25, :lava false})

;; World where sea is a slower path than the grass.
(def test-world-1
  [(Cell. 0 0 0 :grass 0),  (Cell. 1 0 0 :grass 0),  (Cell. 2 0 0 :grass 0), 
   (Cell. 0 1 0 :grass 0),  (Cell. 1 1 0 :lava 0),   (Cell. 2 1 0 :sea 0),
   (Cell. 0 2 0 :grass 0),   (Cell. 1 2 0 :sea 0),    (Cell. 2 2 0 :sea 0)])

(def test-world-value-iterated-1
  [(assoc (Cell. 0 0 0 :grass 0) :value 98), (assoc (Cell. 1 0 0 :grass 0) :value 99),   (assoc (Cell. 2 0 0 :grass 0) :value 100), 
   (assoc (Cell. 0 1 0 :grass 0) :value 97), (assoc (Cell. 1 1 0 :lava 0), :value false) (assoc (Cell. 2 1 0 :sea 0)   :value 85),
   (assoc (Cell. 0 2 0 :grass 0)  :value 96), (assoc (Cell. 1 2 0 :sea 0),  :value 55)    (assoc (Cell. 2 2 0 :sea 0)   :value 70)]) 
  
;; World where lava blocks a fast path  
(def test-world-2
 [(Cell. 0 0 0 :grass 0),  (Cell. 1 0 0 :grass 0), (Cell. 2 0 0 :grass 0), 
  (Cell. 0 1 0 :grass 0),  (Cell. 1 1 0 :lava 0),  (Cell. 2 1 0 :lava 0),
  (Cell. 0 2 0 :grass 0),   (Cell. 1 2 0 :lava 0),  (Cell. 2 2 0 :lava 0)])

(def test-world-value-iterated-2
 [(assoc (Cell. 0 0 0 :grass 0) :value 98), (assoc (Cell. 1 0 0 :grass 0) :value 99),   (assoc (Cell. 2 0 0 :grass 0) :value 100), 
  (assoc (Cell. 0 1 0 :grass 0) :value 97), (assoc (Cell. 1 1 0 :lava 0), :value false) (assoc (Cell. 2 1 0 :lava 0)  :value false),
  (assoc (Cell. 0 2 0 :grass 0)  :value 96), (assoc (Cell. 1 2 0 :lava 0), :value false) (assoc (Cell. 2 2 0 :lava 0)  :value false)])  

;; World where one cell is surrounded by lava.
(def test-world-3
 [(Cell. 0 0 0 :grass 0),  (Cell. 1 0 0 :grass 0), (Cell. 2 0 0 :grass 0), 
  (Cell. 0 1 0 :grass 0),  (Cell. 1 1 0 :lava 0),  (Cell. 2 1 0 :lava  0),
  (Cell. 0 2 0 :grass 0),   (Cell. 1 2 0 :lava 0),  (Cell. 2 2 0 :grass 0)])

(def test-world-value-iterated-3
 [(assoc (Cell. 0 0 0 :grass 0) :value 98), (assoc (Cell. 1 0 0 :grass 0) :value 99),   (assoc (Cell. 2 0 0 :grass 0) :value 100), 
  (assoc (Cell. 0 1 0 :grass 0) :value 97), (assoc (Cell. 1 1 0 :lava 0), :value false) (assoc (Cell. 2 1 0 :lava 0)  :value false),
  (assoc (Cell. 0 2 0 :grass 0)  :value 96), (assoc (Cell. 1 2 0 :lava 0), :value false) (assoc (Cell. 2 2 0 :grass 0) :value false)])

;; World where everything is unknown
(def test-world-4
 [(Cell. 0 0 0 :unknown 0),  (Cell. 1 0 0 :unknown 0), (Cell. 2 0 0 :unknown 0), 
  (Cell. 0 1 0 :unknown 0),  (Cell. 1 1 0 :unknown 0), (Cell. 2 1 0 :unknown 0),
  (Cell. 0 2 0 :unknown 0),  (Cell. 1 2 0 :unknown 0), (Cell. 2 2 0 :unknown 0)])

;; World where everything is unknown
(def test-world-value-iterated-4
 [(assoc (Cell. 0 0 0 :unknown 0) :value 50),  (assoc (Cell. 1 0 0 :unknown 0) :value 75), (assoc (Cell. 2 0 0 :unknown 0), :value 100) 
  (assoc (Cell. 0 1 0 :unknown 0) :value 25),  (assoc (Cell. 1 1 0 :unknown 0) :value 50), (assoc (Cell. 2 1 0 :unknown 0), :value 75)
  (assoc (Cell. 0 2 0 :unknown 0) :value 0),  (assoc (Cell. 1 2 0 :unknown 0) :value 25), (assoc (Cell. 2 2 0 :unknown 0)  :value 50)])

;; World surrounded by false twice removed
(def test-world-5
 [(Cell. 0 0 0 :lava 0),   (Cell. 1 0 0 :lava 0),  (Cell. 2 0 0 :grass 0), 
  (Cell. 0 1 0 :grass 0),  (Cell. 1 1 0 :lava 0),  (Cell. 2 1 0 :lava 0),
  (Cell. 0 2 0 :grass 0),  (Cell. 1 2 0 :grass 0), (Cell. 2 2 0 :grass 0)])

;; World surrounded by false twice removed
(def test-world-value-iterated-5
 [(assoc (Cell. 0 0 0 :lava 0)  :value false),  (assoc (Cell. 1 0 0 :lava 0)  :value false), (assoc (Cell. 2 0 0 :grass 0), :value 100) 
  (assoc (Cell. 0 1 0 :grass 0) :value false),  (assoc (Cell. 1 1 0 :lava 0)  :value false), (assoc (Cell. 2 1 0 :lava 0),  :value false)
  (assoc (Cell. 0 2 0 :grass 0) :value false),  (assoc (Cell. 1 2 0 :grass 0) :value false), (assoc (Cell. 2 2 0 :grass 0)  :value false)])

(deftest value-iteration-test
  (is (= test-world-value-iterated-1 (value-iteration 2 0 movement-mask traversable test-world-1 world-size)))
  (is (= test-world-value-iterated-2 (value-iteration 2 0 movement-mask traversable test-world-2 world-size)))
  (is (= test-world-value-iterated-3 (value-iteration 2 0 movement-mask traversable test-world-3 world-size)))
  (is (= test-world-value-iterated-4 (value-iteration 2 0 movement-mask traversable test-world-4 world-size)))
  (is (= test-world-value-iterated-5 (value-iteration 2 0 movement-mask traversable test-world-5 world-size))))
  
(deftest plan-test
  (is (= [[0 1] [0 0] [1 0] [2 0]] (plan 0 2 2 0 movement traversable test-world-1 world-size)))
  (is (= [[0 1] [0 0] [1 0] [2 0]] (plan 0 2 2 0 movement traversable test-world-2 world-size)))
  (is (= [[0 1] [0 0] [1 0] [2 0]] (plan 0 2 2 0 movement traversable test-world-3 world-size)))
  (is (= [[1 2] [2 2] [2 1] [2 0]] (plan 0 2 2 0 movement traversable test-world-4 world-size)))
  (is (= []                        (plan 0 2 2 0 movement traversable test-world-5 world-size))))