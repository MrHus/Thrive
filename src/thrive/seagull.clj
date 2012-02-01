(ns thrive.seagull
  (:use thrive.actor)
	(:use [thrive.cell :only (find-cell-loc, find-cell, find-cells, cells-with-food, surrounding-cells-by-mask)])
  (:gen-class))

(defrecord Seagull
[
	x      ;; The x position on the world
	y      ;; The y position on the world
	z      ;; The z position on the world, is equal to height of the tile.
])

(def movement [[0, 0], [0, -1] [0, 1] [-1, 0] [1, 0]])
(def traversable {:grass 1, :forest 1, :mountain 1, :desert 1 :sea 1, :unknown 1, :lava 1})

(defn ^Seagull live-seagul
  "A seagul moves to a random place, to annoy people."
  [^Seagull s, actual-world, world-size]
  (let [movement-options (vec (surrounding-cells-by-mask (:x s) (:y s) movement world-size))
       size (dec (count movement-options))
       dest (get movement-options (int (rand size)))]
   (assoc s :x (:x dest) :y (:y dest))))

(defn is-alive?
  [^Seagull s]
  true)

(extend-type Seagull
  Actor
  (live [this world world-size] (live-seagul this (:cells @world) world-size))
  (interval [this] 1000)
  (alive? [this] (is-alive? this)))
