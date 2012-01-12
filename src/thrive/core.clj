(ns thrive.core
  (:use [thrive.actor :only (loop-actor)])
  (:require [thrive.human :only (Human)])
  (:import (thrive.human Human))
  (:require [thrive.city :only (City)])
  (:import (thrive.city City))
  (:use [thrive.cell :only (generate-unknown-world)])
  (:require [thrive.cell :only (Cell)])
  (:import (thrive.cell Cell))
  (:gen-class))

;;;;;;; References ;;;;;;;

(def world-size 10)

(def unknown-world (generate-unknown-world world-size))

(def test-world
  [{:x 0, :y 0, :z 0, :tile :grass, :food 0}
   {:x 1, :y 0, :z 0, :tile :grass, :food 0}
   {:x 2, :y 0, :z 0, :tile :lava, :food 0}
   {:x 3, :y 0, :z 0, :tile :grass, :food 0}
   {:x 4, :y 0, :z 0, :tile :sea, :food 0}
   {:x 5, :y 0, :z 0, :tile :grass, :food 0}
   {:x 6, :y 0, :z 0, :tile :grass, :food 0}
   {:x 7, :y 0, :z 0, :tile :grass, :food 0}
   {:x 8, :y 0, :z 0, :tile :grass, :food 0}
   {:x 9, :y 0, :z 0, :tile :grass, :food 0}
 
   {:x 0, :y 1, :z 0, :tile :grass, :food 0}
   {:x 1, :y 1, :z 0, :tile :grass, :food 0}
   {:x 2, :y 1, :z 0, :tile :grass, :food 0}
   {:x 3, :y 1, :z 0, :tile :grass, :food 0}
   {:x 4, :y 1, :z 0, :tile :sea, :food 0}
   {:x 5, :y 1, :z 0, :tile :grass, :food 0}
   {:x 6, :y 1, :z 0, :tile :grass, :food 0}
   {:x 7, :y 1, :z 0, :tile :grass, :food 0}
   {:x 8, :y 1, :z 0, :tile :grass, :food 0}
   {:x 9, :y 1, :z 0, :tile :grass, :food 0}
 
   {:x 0, :y 2, :z 0, :tile :lava, :food 0}
   {:x 1, :y 2, :z 0, :tile :lava, :food 0}
   {:x 2, :y 2, :z 0, :tile :lava, :food 0}
   {:x 3, :y 2, :z 0, :tile :grass, :food 0}
   {:x 4, :y 2, :z 0, :tile :sea, :food 0}
   {:x 5, :y 2, :z 0, :tile :grass, :food 0}
   {:x 6, :y 2, :z 0, :tile :grass, :food 0}
   {:x 7, :y 2, :z 0, :tile :grass, :food 0}
   {:x 8, :y 2, :z 0, :tile :grass, :food 0}
   {:x 9, :y 2, :z 0, :tile :grass, :food 0}
 
   {:x 0, :y 3, :z 0, :tile :grass, :food 0}
   {:x 1, :y 3, :z 0, :tile :grass, :food 0}
   {:x 2, :y 3, :z 0, :tile :grass, :food 0}
   {:x 3, :y 3, :z 0, :tile :grass, :food 0}
   {:x 4, :y 3, :z 0, :tile :sea, :food 0}
   {:x 5, :y 3, :z 0, :tile :grass, :food 0}
   {:x 6, :y 3, :z 0, :tile :grass, :food 0}
   {:x 7, :y 3, :z 0, :tile :grass, :food 0}
   {:x 8, :y 3, :z 0, :tile :desert, :food 0}
   {:x 9, :y 3, :z 0, :tile :desert, :food 0}
 
   {:x 0, :y 4, :z 0, :tile :grass, :food 0}
   {:x 1, :y 4, :z 0, :tile :grass, :food 0}
   {:x 2, :y 4, :z 0, :tile :grass, :food 0}
   {:x 3, :y 4, :z 0, :tile :grass, :food 0}
   {:x 4, :y 4, :z 0, :tile :sea, :food 0}
   {:x 5, :y 4, :z 0, :tile :grass, :food 0}
   {:x 6, :y 4, :z 0, :tile :grass, :food 0}
   {:x 7, :y 4, :z 0, :tile :grass, :food 0}
   {:x 8, :y 4, :z 0, :tile :desert, :food 0}
   {:x 9, :y 4, :z 0, :tile :desert, :food 0}
 
   {:x 0, :y 5, :z 0, :tile :grass, :food 0}
   {:x 1, :y 5, :z 4, :tile :mountain, :food 0}
   {:x 2, :y 5, :z 0, :tile :grass, :food 0}
   {:x 3, :y 5, :z 0, :tile :grass, :food 0}
   {:x 4, :y 5, :z 0, :tile :sea, :food 0}
   {:x 5, :y 5, :z 0, :tile :sea, :food 0}
   {:x 6, :y 5, :z 0, :tile :grass, :food 50}
   {:x 7, :y 5, :z 0, :tile :sea, :food 0}
   {:x 8, :y 5, :z 0, :tile :sea, :food 0}
   {:x 9, :y 5, :z 0, :tile :sea, :food 0}
 
   {:x 0, :y 6, :z 0, :tile :grass, :food 0}
   {:x 1, :y 6, :z 3, :tile :mountain, :food 0}
   {:x 2, :y 6, :z 0, :tile :grass, :food 0}
   {:x 3, :y 6, :z 0, :tile :grass, :food 0}
   {:x 4, :y 6, :z 0, :tile :grass, :food 0}
   {:x 5, :y 6, :z 0, :tile :grass, :food 0}
   {:x 6, :y 6, :z 0, :tile :grass, :food 0}
   {:x 7, :y 6, :z 0, :tile :grass, :food 0}
   {:x 8, :y 6, :z 0, :tile :grass, :food 0}
   {:x 9, :y 6, :z 0, :tile :grass, :food 0}
 
   {:x 0, :y 7, :z 0, :tile :grass, :food 0}
   {:x 1, :y 7, :z 2, :tile :mountain, :food 0}
   {:x 2, :y 7, :z 0, :tile :grass, :food 0}
   {:x 3, :y 7, :z 2, :tile :mountain, :food 0}
   {:x 4, :y 7, :z 2, :tile :mountain, :food 0}
   {:x 5, :y 7, :z 2, :tile :mountain, :food 0}
   {:x 6, :y 7, :z 3, :tile :mountain, :food 0}
   {:x 7, :y 7, :z 2, :tile :mountain, :food 0}
   {:x 8, :y 7, :z 1, :tile :mountain, :food 0}
   {:x 9, :y 7, :z 1, :tile :mountain, :food 0}
 
   {:x 0, :y 8, :z 0, :tile :grass, :food 0}
   {:x 1, :y 8, :z 1, :tile :mountain, :food 0}
   {:x 2, :y 8, :z 0, :tile :grass, :food 0}
   {:x 3, :y 8, :z 1, :tile :mountain, :food 0}
   {:x 4, :y 8, :z 0, :tile :grass, :food 0}
   {:x 5, :y 8, :z 0, :tile :grass, :food 0}
   {:x 6, :y 8, :z 0, :tile :grass, :food 0}
   {:x 7, :y 8, :z 0, :tile :grass, :food 0}
   {:x 8, :y 8, :z 0, :tile :grass, :food 0}
   {:x 9, :y 8, :z 0, :tile :desert, :food 0}
 
   {:x 0, :y 9, :z 0, :tile :grass, :food 0}
   {:x 1, :y 9, :z 0, :tile :grass, :food 0}
   {:x 2, :y 9, :z 0, :tile :grass, :food 0}
   {:x 3, :y 9, :z 1, :tile :mountain, :food 0}
   {:x 4, :y 9, :z 0, :tile :grass, :food 0}
   {:x 5, :y 9, :z 0, :tile :grass, :food 0}
   {:x 6, :y 9, :z 0, :tile :grass, :food 0}
   {:x 7, :y 9, :z 0, :tile :grass, :food 0}
   {:x 8, :y 9, :z 0, :tile :grass, :food 0}
   {:x 9, :y 9, :z 0, :tile :desert, :food 0}])

(def world (ref 
  {
    :cells  test-world
    :actors [
      (agent (City. 9 1 0 50 unknown-world)) 
      (agent (Human. 0 0 0 5 unknown-world [9 1] :find-food :stay)) 
      (agent (Human. 0 5 0 5 test-world [9 1] :find-food :stay))]
  }))

(defn live-world
  "Sets the actors in motion."
  []  
  (doseq [actor (:actors @world)]
    (send-off actor loop-actor world world-size)))    