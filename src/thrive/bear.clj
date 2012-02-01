(ns thrive.bear
  (:use thrive.actor)
	(:use [thrive.cell :only (find-cell-loc, find-cell, find-cells, cells-with-food, surrounding-cells-by-mask)])
  (:gen-class))

(defrecord Bear
[
	x      ;; The x position on the world
	y      ;; The y position on the world
	z      ;; The z position on the world, is equal to height of the tile.
])

(def movement [[0, 0], [0, -1] [0, 1] [-1, 0] [1, 0]])
(def traversable {:grass false, :forest 1, :mountain false, :desert false :sea false, :unknown 1, :lava false})

(defn ^Bear live-bear
  "A bear moves to a random place, in the woods, to eat people."
	[^Bear s, actual-world, world-size]
	(let [movement-options (vec (surrounding-cells-by-mask (:x s) (:y s) movement world-size))
			  size (dec (count movement-options))
				dest (get movement-options (int (rand size)))]
		(assoc s :x (:x dest) :y (:y dest))))

(defn is-alive?
  [^Bear s]
  true)

(extend-type Bear
  Actor
  (live [this world world-size] (live-bear this (:cells @world) world-size))
  (interval [this] 1000)
  (alive? [this] (is-alive? this)))
