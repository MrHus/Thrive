(ns thrive.core
  (:use thrive.actors)
  (:import (thrive.actors Person))
  (:gen-class))

;;;;;;; Records ;;;;;;;

(defrecord Cell
[
  x 	  ;; The x position on the world 
 	y 	  ;; The y position on the world
	z	    ;; The z position on the world, is equal to height of the tile.
 	tile 	;; The type of tile, in symbol form. IE :grass, :sea, :mountain
	food  ;; The current food value of this cell
])

;;;;;;; References ;;;;;;;

(def world-width-height 3)

(def unknown-world
  [(Cell. 0 0 0 :unknown 0), (Cell. 1 0 0 :unknown 0), (Cell. 2 0 0 :unknown 0), 
   (Cell. 0 1 0 :unknown 0), (Cell. 1 1 0 :unknown 0), (Cell. 2 1 0 :unknown 0),
   (Cell. 0 2 0 :unknown 0), (Cell. 1 2 0 :unknown 0), (Cell. 2 2 0 :unknown 0)])

(def world (ref 
  {:cells 
    [(Cell. 0 0 0 :sea 0), (Cell. 1 0 0 :grass 0),    (Cell. 2 0 0 :grass 0), 
     (Cell. 0 1 0 :sea 0), (Cell. 1 1 0 :sea 0),      (Cell. 2 1 0 :mountain 0),
     (Cell. 0 2 0 :sea 0), (Cell. 1 2 0 :mountain 0), (Cell. 2 2 0 :lava 0)]
   
   :actors
    [(agent (Person. 0 1 0 5 unknown-world))]
  }))

(defn live-world
  [w]  
  (doseq [actor (:actors w)]
    (send-off actor loop-actor)))
      