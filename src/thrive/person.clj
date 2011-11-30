(ns thrive.person
  (:use thrive.actor)
  (:gen-class))

(def world-width-height 3)

(defn find-cells
  "Find all cells in the actual world from the coll, which is a collection of [{:x, :y}]"
  [actual-world coll]
  (map #(nth actual-world %) (map #(+ (:x %) (* world-width-height (:y %))) coll)))

(defn neighbors
  "Gets the neighbors of a cell, but within the bounds of the world. Returns [{:x, :y} ...]"
  [x, y]
  (filter 
    #(and (>= (:x %) 0) (< (:x %) world-width-height) (>= (:y %) 0) (< (:y %) world-width-height))
    [{:x x, :y y}, {:x (dec x), :y y}, {:x (inc x), :y y}, {:x x, :y (dec y)}, {:x x, :y (inc y)}]))
      
(defn observe
  "A person can observe left, right up, and down"
  [^Person p, actual-world]
  (let [observed-cells (find-cells actual-world (neighbors (:x p) (:y p)))
        p-world (reduce #(assoc %1 (+ (:x %2) (* world-width-height (:y %2))) %2) (:world p) observed-cells)]
    (assoc p :world p-world)))

(defn live-person
  "A person first observers his surroundings than makes a move."
  [^Person p, actual-world]
  (observe p actual-world))
  
(defrecord Person
[
  x 	  ;; The x position on the world 
  y 	  ;; The y position on the world
  z	    ;; The z position on the world, is equal to height of the tile.
  food  ;; The current food this person is carrying.
  world ;; The current world as this person sees it.
]
  Actor
  (live [this world] (live-person this (:cells @world)))
  (interval [this] 1000))  