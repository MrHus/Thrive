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
  
  (:gen-class))

;;;;;;; References ;;;;;;;

(def world-size 10)

(def unknown-world (generate-unknown-world world-size))

(def test-world
  [(Cell.  0,  0,  0,  :grass,  0)
   (Cell.  1,  0,  0,  :grass,  0)
   (Cell.  2,  0,  0,  :lava,   0)
   (Cell.  3,  0,  0,  :grass,  0)
   (Cell.  4,  0,  0,  :sea,    0)
   (Cell.  5,  0,  0,  :grass,  0)
   (Cell.  6,  0,  0,  :grass,  0)
   (Cell.  7,  0,  0,  :grass,  0)
   (Cell.  8,  0,  0,  :grass,  0)
   (Cell.  9,  0,  0,  :grass,  0)
 
   (Cell.  0,  1,  0,  :grass,  0)
   (Cell.  1,  1,  0,  :grass,  0)
   (Cell.  2,  1,  0,  :grass,  0)
   (Cell.  3,  1,  0,  :grass,  0)
   (Cell.  4,  1,  0,  :sea,    0)
   (Cell.  5,  1,  0,  :grass,  0)
   (Cell.  6,  1,  0,  :grass,  0)
   (Cell.  7,  1,  0,  :grass,  0)
   (Cell.  8,  1,  0,  :grass,  0)
   (Cell.  9,  1,  0,  :grass,  0)
 
   (Cell.  0,  2,  0,  :lava,   0)
   (Cell.  1,  2,  0,  :lava,   0)
   (Cell.  2,  2,  0,  :lava,   0)
   (Cell.  3,  2,  0,  :grass,  0)
   (Cell.  4,  2,  0,  :sea,    0)
   (Cell.  5,  2,  0,  :grass,  0)
   (Cell.  6,  2,  0,  :grass,  0)
   (Cell.  7,  2,  0,  :grass,  0)
   (Cell.  8,  2,  0,  :grass,  0)
   (Cell.  9,  2,  0,  :grass,  0)
 
   (Cell.  0,  3,  0,  :grass,  0)
   (Cell.  1,  3,  0,  :grass,  0)
   (Cell.  2,  3,  0,  :grass,  0)
   (Cell.  3,  3,  0,  :grass,  0)
   (Cell.  4,  3,  0,  :sea,    0)
   (Cell.  5,  3,  0,  :grass,  0)
   (Cell.  6,  3,  0,  :grass,  0)
   (Cell.  7,  3,  0,  :grass,  0)
   (Cell.  8,  3,  0,  :desert, 0)
   (Cell.  9,  3,  0,  :desert, 0)
 
   (Cell.  0,  4,  0,  :grass,  0)
   (Cell.  1,  4,  0,  :grass,  0)
   (Cell.  2,  4,  0,  :grass,  0)
   (Cell.  3,  4,  0,  :grass,  0)
   (Cell.  4,  4,  0,  :sea,    0)
   (Cell.  5,  4,  0,  :grass,  0)
   (Cell.  6,  4,  0,  :grass,  0)
   (Cell.  7,  4,  0,  :grass,  0)
   (Cell.  8,  4,  0,  :desert, 0)
   (Cell.  9,  4,  0,  :desert, 0)
 
   (Cell.  0,  5,  0,  :grass,  0)
   (Cell.  1,  5,  4,  :mountain-3, 0)
   (Cell.  2,  5,  0,  :grass,  0)
   (Cell.  3,  5,  0,  :grass,  0)
   (Cell.  4,  5,  0,  :sea,    0)
   (Cell.  5,  5,  0,  :sea,    0)
   (Cell.  6,  5,  0,  :grass,  50)
   (Cell.  7,  5,  0,  :sea,    0)
   (Cell.  8,  5,  0,  :sea,    0)
   (Cell.  9,  5,  0,  :sea,    0)
 
   (Cell.  0,  6,  0,  :grass,  0)
   (Cell.  1,  6,  3,  :mountain-2, 0)
   (Cell.  2,  6,  0,  :grass,  0)
   (Cell.  3,  6,  0,  :grass,  0)
   (Cell.  4,  6,  0,  :grass,  0)
   (Cell.  5,  6,  0,  :grass,  0)
   (Cell.  6,  6,  0,  :grass,  0)
   (Cell.  7,  6,  0,  :grass,  0)
   (Cell.  8,  6,  0,  :grass,  0)
   (Cell.  9,  6,  0,  :grass,  0)
 
   (Cell.  0,  7,  0,  :grass,     0)
   (Cell.  1,  7,  2,  :mountain-1,  0)
   (Cell.  2,  7,  0,  :grass,     0)
   (Cell.  3,  7,  2,  :mountain-2,  0)
   (Cell.  4,  7,  2,  :mountain-1,  0)
   (Cell.  5,  7,  2,  :mountain-2,  0)
   (Cell.  6,  7,  3,  :mountain-2,  0)
   (Cell.  7,  7,  2,  :forest,  0)
   (Cell.  8,  7,  1,  :forest,  0)
   (Cell.  9,  7,  1,  :forest,  0)
 
   (Cell.  0,  8,  0,  :grass,     0)
   (Cell.  1,  8,  1,  :mountain-1,  0)
   (Cell.  2,  8,  0,  :grass,     0)
   (Cell.  3,  8,  1,  :mountain-1,  0)
   (Cell.  4,  8,  0,  :grass,  0)
   (Cell.  5,  8,  0,  :grass,  0)
   (Cell.  6,  8,  0,  :grass,  0)
   (Cell.  7,  8,  0,  :forest,  0)
   (Cell.  8,  8,  0,  :forest,  0)
   (Cell.  9,  8,  0,  :forest,  0)
 
   (Cell.  0,  9,  0,  :grass,  0)
   (Cell.  1,  9,  0,  :grass,  0)
   (Cell.  2,  9,  0,  :grass,  0)
   (Cell.  3,  9,  1,  :mountain-1,  0)
   (Cell.  4,  9,  0,  :grass,  0)
   (Cell.  5,  9,  0,  :grass,  0)
   (Cell.  6,  9,  0,  :grass,  0)
   (Cell.  7,  9,  0,  :forest,  0)
   (Cell.  8,  9,  0,  :forest,  0)
   (Cell.  9,  9,  0,  :forest,  0)])

(def world (ref 
{
    :cells  test-world
    :actors [
      (agent (City. 9 1 0 50 unknown-world)) 
      (agent (Human. 0 0 0 5 unknown-world [9 1] :scout [] :a*))
      (agent (Human. 3 3 0 0 unknown-world [9 1] :scout [] :mdp))
      (agent (Human. 0 8 0 155 test-world [9 1] :city [] :a*))
      (agent (Human. 8 8 0 5 unknown-world [9 1] :city [] :a*))
      (agent (Seagull. 0 0 0 true))
      (agent (Seagull. 5 5 0 true))
      (agent (Seagull. 0 9 0 true))
      (agent (Bear. 8 8 0))
    ]
}))

(defn cleanup-dead
  "Removes dead actors in the world."
  []
  ;(watch-actors)
  ;(assoc @world :actors (living-actors (:actors @world)))
  (do
    (println "CLEANUP-DEAD in capitials to find it")
  (dosync 
    (let [alter-actors (filter #(alive? @%) (:actors @world))]
      (alter world assoc :actors alter-actors)))))

(defn watch-actors
  "Teh world subscribes to changes of it's actors."
  []
  (doseq [actor (:actors @world)]
    (add-watch actor nil (fn [k r o n] cleanup-dead))))
 
    
;  (dotimes [i (count (:actors @world))]
 ;   (add-watch
 ;;    ((:actors @world) i)
 ;    :alive
 ;    (fn [k r o n]
 ;      (do
 ;        ;(println "key => "  k)
 ;        ;(println "reference => "  r)
 ;        ;(println "old => " o)
 ;        ;(println "new => " n)
 ;        (if (false? (alive? n))
 ;          (do
 ;            (println "Dead")
 ;            (remove r (:actors @world)))))))))

(defn live-world
  "Sets the actors in motion."
  []  
  (do
    (doseq [actor (:actors @world)]
      (send-off actor loop-actor world world-size))
    (watch-actors)))
