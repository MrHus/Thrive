(ns thrive.bear
  (:use thrive.actor)
  (:use [thrive.cell])
  (:require [thrive.cell :only (Cell)])
  (:import (thrive.cell Cell))
  (:gen-class))

(defrecord Bear
[
	x      ;; The x position on the world
	y      ;; The y position on the world
	z      ;; The z position on the world, is equal to height of the tile.
])

(def movement {:left [-1, 0], :right [1, 0], :up [0, -1], :down [0, 1]})
(def traversable {:grass false :forest 1 :mountain false :desert false :sea false :unknown 1 :lava false})

(defn ^Bear live-bear
  "A bear moves to a random place, in the woods, to eat people."
  [^Bear s, actual-world, world-size]
  (let [movement-options (vec (map #(vector (:x %) (:y %)) (find-moveable-cells [(:x s) (:y s)] movement traversable actual-world world-size)))
        size  (count movement-options)
        [x y] (get movement-options (rand-int size))]
    (assoc s :x x :y y)))

(defn is-alive?
  [^Bear s]
  true)

(extend-type Bear
  Actor
  (live [this world world-size] (live-bear this (:cells @world) world-size))
  (interval [this] 1000)
  (alive? [this] (is-alive? this)))
