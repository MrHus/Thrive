(ns thrive.city
  (:use thrive.actor)
  (:gen-class))

(defrecord City
[
  x      ;; The x position on the world
  y      ;; The y position on the world
  z      ;; The z position on the world, is equal to height of the tile.
  food   ;; The current food this city has stored.
  world  ;; The current world as this city (society) knows it.
])

(defn decrease-food
  [^City c amount]
  (let [food (- (:food c) amount)
        new-food (if (< food 0) 0 food)]
   (assoc c :food new-food)))

(defn deliver-food
  [^City c, amount]
  (println "Got food")
  (assoc c :food (+ (:food c) amount)))

(defn ^City live-city
  "A human first observers his surroundings than makes a move."
  [^City c, actual-world] 
  (decrease-food c 5))

(defn is-alive?
  [^City c]
  (> (:food c) 0))

(extend-type City
  Actor
  (live [this world world-size] (live-city this (:cells @world)))
  (interval [this] 30000)
  (alive? [this] (is-alive? this)))
