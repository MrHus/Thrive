(ns thrive.actors
  (:gen-class))

(defprotocol Actor
  "Defines what an Actor should respond to."
  (live     [this] "The live method, IE what the actor does")
  (interval [this] "The interval in which the actor performs live"))

(defn loop-actor
  "This starts the actors loop. It performs the actors 'live' function each 'interval'"
  [actor]
  (do
    (. Thread sleep (interval actor))
    (send-off *agent* loop-actor))
  (live actor))

(defrecord Person
[
  x 	  ;; The x position on the world 
  y 	  ;; The y position on the world
  z	    ;; The z position on the world, is equal to height of the tile.
  food  ;; The current food this person is carrying.
  world ;; The current world as this person sees it.
]
  Actor
  (live 
    [this]
    (do
      (println "living the good life")
      this))

  (interval 
    [this] 1000))
 