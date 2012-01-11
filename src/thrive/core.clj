(ns thrive.core
  (:use thrive.actor)
  (:require [thrive.human :only (Human)])
  (:import (thrive.human Human))
  (:require [thrive.city :only (City)])
  (:import (thrive.city City))
  (:gen-class))

;;;;;;; Records ;;;;;;;

(defrecord Cell
[
  x     ;; The x position on the world 
  y     ;; The y position on the world
  z     ;; The z position on the world, is equal to height of the tile.
  tile  ;; The type of tile, in symbol form. IE :grass, :sea, :mountain
  food  ;; The current food value of this cell
])

;;;;;;; References ;;;;;;;

(defn generate-unkown-world
  "Generates an uknown square world the size is determined by the first argument"
  [size]
  (vec (map #(Cell. (mod % size) (int (/ % size)) 0 :unknown 0) (range (* size size)))))

(def unknown-world (generate-unkown-world 3))

(def world (ref 
  {:cells 
    [(Cell. 0 0 0 :grass 0),  (Cell. 1 0 0 :grass 15),    (Cell. 2 0 0 :grass 15), 
     (Cell. 0 1 0 :sea 0),    (Cell. 1 1 0 :sea 0),       (Cell. 2 1 0 :mountain 0),
     (Cell. 0 2 0 :sea 0),    (Cell. 1 2 0 :mountain 0),  (Cell. 2 2 0 :lava 0)]
   
   :actors
    [(agent (City. 0 0 0 50 unknown-world)) (agent (Human. 0 1 0 5 unknown-world))]
  }))

(defn live-world
  "Sets the actors in motion."
  []  
  (doseq [actor (:actors @world)]
    (send-off actor loop-actor world)))    