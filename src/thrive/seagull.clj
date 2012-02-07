(ns thrive.seagull
  (:use thrive.actor)
  (:require [thrive.human :only (Human)])
  (:import (thrive.human Human))
  (:use [thrive.cell :only (find-cell-loc, find-cell, find-cells, cells-with-food, surrounding-cells-by-mask)])
  (:gen-class))

(defrecord Seagull
[
   x      ;; The x position on the world
   y      ;; The y position on the world
   z      ;; The z position on the world, is equal to height of the tile.
   alive  ;; is Seagull alive
])

(def movement [[0, 0], [0, -1] [0, 1] [-1, 0] [1, 0]])
(def traversable {:grass 1, :forest 1, :mountain-1 1, :mountain-2 1, :mountain-3 1, :desert 1 :sea 1, :unknown 1, :lava 1})
(def worth-food-loot 50)

(defn ^Seagull alter-alive
  "If a Human is on the same location as a Seagull. It will get terminated.
   Such is the cruel destiny of the seagull."
  [^Seagull s, ref-world, world-size]
  (let [alive (zero? (count (filter #(and (instance? Human @%) (= (:x s) (:x @%)) (= (:y s) (:y @%))) (:actors @ref-world))))]
    (if (false? alive)
      (do
        (dosync 
          (let [cells  (:cells @ref-world)
                loc    (find-cell-loc (:x s) (:y s) world-size)
                cell   (find-cell (:x s) (:y s) @ref-world world-size)]
            (alter ref-world assoc :cells 
              (assoc cells loc 
                (assoc cell :food (+ 5 (:food cell)))))))
        (assoc s :alive true))      
      (assoc s :alive false))))        

(defn ^Seagull move-seagull
  "Move the Seagull to a random location that surrounds it."
  [^Seagull s, world-size]
  (let [movement-options (vec (surrounding-cells-by-mask (:x s) (:y s) movement world-size))
        size (count movement-options)
        dest (get movement-options (rand-int size))]
    (assoc s :x (:x dest) :y (:y dest))))
               
(defn ^Seagull live-seagull
  "A seagul moves to a random place, to annoy people. When it hits a human it promptly dies."
  [^Seagull s, ref-world, world-size]
  (let [alive-seagull (alter-alive s ref-world world-size)]
    (if (:alive alive-seagull)
      (move-seagull alive-seagull world-size)
      alive-seagull)))
  
(defn is-alive?
  [^Seagull s]
  (:alive s))

(extend-type Seagull
  Actor
  (live [this world world-size] (live-seagull this world world-size))
  (interval [this] 1000)
  (alive? [this] (is-alive? this)))