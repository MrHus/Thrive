(ns thrive.core
  (:use [thrive.actor :only (loop-actor, living-actors, alive?)])
  (:require [thrive.human :only (Human)])
  (:import (thrive.human Human))
  (:require [thrive.city :only (City)])
  (:import (thrive.city City))
  (:use [thrive.cell :only (generate-unknown-world)])
  (:require [thrive.cell :only (Cell)])
  (:import (thrive.cell Cell))
  
  (:require [thrive.seagull :only (Seagull)])
  (:import (thrive.seagull Seagull))

  (:require [thrive.bear :only (Bear)])
  (:import (thrive.bear Bear))
  
  (:use [overtone.at-at])
  
  (:gen-class))

;;;;;;; References ;;;;;;;

(load "examples/d-double-world-pathfinding-blind-example")

(defn cleanup-dead
  "Removes dead actors in the world."
  []  
  (dosync 
    (alter world assoc :actors (filter #(alive? @%) (:actors @world)))))

(defn live-world
  "Sets the actors in motion."
  []  
  (do
    (every 1000 cleanup-dead) ;; Clean the actors up every second. 
    (doseq [actor (:actors @world)]
      (send-off actor loop-actor world world-size))))
