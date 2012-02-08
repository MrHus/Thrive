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
])

(def movement [[0, 0], [0, -1] [0, 1] [-1, 0] [1, 0]])
(def traversable {:grass 1, :forest 1, :mountain-1 1, :mountain-2 1, :mountain-3 1, :desert 1 :sea 1, :unknown 1, :lava 1})
(def worth-food-loot 50)

(defn ^Seagull alter-alive
  "If a Human is on the same location as a Seagull. It will get terminated.
   Such is the cruel destiny of the seagull."
  [^Seagull s, ref-actors ref-world, world-size]
  (let [alive (zero? (count (filter #(and (instance? Human @%) (= (:x s) (:x @%)) (= (:y s) (:y @%))) @ref-actors)))]
    (if (false? alive)
      (do
        (dosync 
          (let [loc    (find-cell-loc (:x s) (:y s) world-size)
                cell   (find-cell (:x s) (:y s) @ref-world world-size)]
            (alter ref-world assoc loc 
                (assoc cell :food (+ worth-food-loot (:food cell))))))
        (assoc s :x (int (rand world-size)) :y (int (rand world-size))))      
     s)))        

(defn ^Seagull move-seagull
  "Move the Seagull to a random location that surrounds it."
  [^Seagull s, world-size]
  (let [movement-options (vec (surrounding-cells-by-mask (:x s) (:y s) movement world-size))
        size (count movement-options)
        dest (get movement-options (rand-int size))]
    (assoc s :x (:x dest) :y (:y dest))))
               
(defn ^Seagull live-seagull
  "A seagul moves to a random place, to annoy people. When it hits a human it promptly dies."
  [^Seagull s, ref-actors ref-world, world-size]
  (let [alive-seagull (alter-alive s ref-actors ref-world world-size)]
    (if (:alive alive-seagull)
      (move-seagull alive-seagull world-size)
      alive-seagull)))
  
(defn is-alive?
  "Seagull's are immortal beings sent from the future to annoy.
   But in al seriousness a seagull is reused when it 'dies' it 
   gets a new location."
  [^Seagull s]
  true)

(extend-type Seagull
  Actor
  (live [this actors world world-size] (live-seagull this actors world world-size))
  (interval [this] 1000)
  (alive? [this] (is-alive? this)))