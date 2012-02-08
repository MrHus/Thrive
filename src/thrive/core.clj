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

;(load "examples/d-double-world-pathfinding-seen-example")
;(load "examples/d-double-world-pathfinding-blind-example")
;(load "examples/d-world-with-bears")
;(load "examples/d-world-with-seagull")
(load "examples/example1")
;(load "examples/c-unknown-lava-world")
;(load "examples/c-medium-unknown-world")
;(load "examples/c-small-unknown-world")
;(load "examples/c-unknown-through-middle-then-around")
;(load "examples/c-small-move-around-sea")

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
