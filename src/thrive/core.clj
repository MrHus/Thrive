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

(def unknown-world (generate-unknown-world 3))

(def world (ref 
  {:cells 
    [(Cell. 0 0 0 :grass 0),  (Cell. 1 0 0 :grass 15),    (Cell. 2 0 0 :grass 15), 
     (Cell. 0 1 0 :sea 0),    (Cell. 1 1 0 :sea 0),       (Cell. 2 1 0 :mountain 0),
     (Cell. 0 2 0 :sea 0),    (Cell. 1 2 0 :mountain 0),  (Cell. 2 2 0 :lava 0)]
   
   :actors
    [(agent (City. 0 0 0 50 unknown-world)) (agent (Human. 0 1 0 5 unknown-world [0 0]))]
  }))

(defn live-world
  "Sets the actors in motion."
  []  
  (doseq [actor (:actors @world)]
    (send-off actor loop-actor world)))    