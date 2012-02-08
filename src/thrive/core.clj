(ns thrive.core
  (:use [thrive.actor :only (loop-actor, alive?)])
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

(def world-size 10)

(def unknown-world (generate-unknown-world world-size))   

(load "examples/d-world-with-bears")

(defn cleanup-dead
  "Removes dead actors in the world."
  []  
  (do
    ;(println "Start cleanup of dead actors")
    (dosync 
      (ref-set actors (filter #(alive? @%) @actors)))))

(defn live-world
  "Sets the actors in motion."
  []  
  (do
    (every 1000 cleanup-dead) ;; Clean the actors up every second. 
    (doseq [actor @actors]
      (send-off actor loop-actor actors world world-size))))
