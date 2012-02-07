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

(defn ^Seagull live-seagul
  "A seagul moves to a random place, to annoy people."
  [^Seagull s, actual-world, world-size]
  (let [movement-options (vec (surrounding-cells-by-mask (:x s) (:y s) movement world-size))
        size (count movement-options)
        dest (get movement-options (rand-int size))]
    (let [x (:x s) 
          y (:y s)
          alive (zero? (count (filter #(and (instance? Human @%) (= x (:x @%)) (= (:y @%))) (:actors actual-world))))] 
      (if alive
        (assoc s :x (:x dest) :y (:y dest))
        (do
          (dosync 
            (let [cells(:cells actual-world)
                  item-number (find-cell-loc x y world-size)
                  used-item (find-cell x y cells world-size)
                  alter-cells (assoc-in cells [item-number] (assoc used-item :food worth-food-loot ))]
              (alter actual-world assoc :cells alter-cells)))
          (assoc s :alive alive))
        ))))

(defn is-alive?
  [^Seagull s]
  (:alive s))

(extend-type Seagull
  Actor
  (live [this world world-size] (live-seagul this @world world-size))
  (interval [this] 1000)
  (alive? [this] (is-alive? this)))