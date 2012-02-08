(ns thrive.city
  (:use thrive.actor)
  (:use [thrive.cell :only (share-knowledge-between)])
  
  (:require [thrive.human :only (Human)])
  (:import (thrive.human Human))
   
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

;(reduce share-knowledge-between '([{:time 40} {:time 1}] [{:time 20} {:time 10}] [{:time 99} {:time 2}])) 

(defn ^City live-city
  "A city burns food"
  [^City c ref-actors] 
  (let [city-dwellers (filter #(and (instance? Human @%) (= (:x c) (:x @%)) (= (:y c) (:y @%))) @ref-actors)]
    (if (zero? (count city-dwellers))
      (decrease-food c 1)
      (assoc (decrease-food c 1) :world (share-knowledge-between (:world c) (reduce share-knowledge-between (map #(:world @%) city-dwellers)))))))

(defn is-alive?
  [^City c]
  (> (:food c) 0))

(extend-type City
  Actor
  (live [this actors world world-size] (live-city this actors))
  (interval [this] 1000)
  (alive? [this] (is-alive? this)))
