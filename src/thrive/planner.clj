(ns thrive.planner
  (:use [thrive.mdp :only (plan) :as mdp])
  (:use [thrive.astar.astar :only (get-path-a*) :as astar])
  )

(defn get-plan
  "Provides a path to a location"
  [algo x y dx dy movement traversable world world-size]
  (if (= :mdp algo)
    (mdp/plan x y dx dy movement traversable world world-size)
    (if (= :a* algo)
      []
      ;(astar/get-path-a* [x y] [dx dy] movement traversable world world-size)
      )))