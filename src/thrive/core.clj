(ns thrive.core)

;;;;;;; Records ;;;;;;;

(defrecord Cell
[
  x 	  ;; The x position on the world 
 	y 	  ;; The y position on the world
	z	    ;; The z position on the world, is equal to height of the tile.
 	tile 	;; The type of tile, in symbol form. IE :grass, :sea, :mountain
	food  ;; The current food value of this cell
])

;;;;;;; Protocols ;;;;;;;

(defprotocol Actor
"Defines what an Actor should respond to."
  (live     [this] "The live method, IE what the actor does")
  (interval [this] "The interval in which the actor performs live"))
  
(defrecord Person
[
  x 	  ;; The x position on the world 
  y 	  ;; The y position on the world
  z	    ;; The z position on the world, is equal to height of the tile.
  food  ;; The current food this person is carrying.
  w     ;; The current world as this person sees it.
]
  Actor
  (live 
    [this]
    (do
      (println "living the good life")
      this))
  
  (interval 
    [this] 1000))  

;;;;;;; References ;;;;;;;

(def world-width-height 3)

(def unkown-world
  [(Cell. 0 0 0 :unkown 0), (Cell. 1 0 0 :unkown 0), (Cell. 2 0 0 :unkown 0), 
   (Cell. 0 1 0 :unkown 0), (Cell. 1 1 0 :unkown 0), (Cell. 2 1 0 :unkown 0),
   (Cell. 0 2 0 :unkown 0), (Cell. 1 2 0 :unkown 0), (Cell. 2 2 0 :unkown 0)])

(def world (ref 
  {:cells 
    [(Cell. 0 0 0 :sea 0), (Cell. 1 0 0 :grass 0),    (Cell. 2 0 0 :grass 0), 
     (Cell. 0 1 0 :sea 0), (Cell. 1 1 0 :sea 0),      (Cell. 2 1 0 :mountain 0),
     (Cell. 0 2 0 :sea 0), (Cell. 1 2 0 :mountain 0), (Cell. 2 2 0 :lava 0)]
   
   :actors
    [(agent (Person. 0 1 0 5 unkown-world))]
  }))

(defn loop-actor
  "This starts the actors loop. It performs the actors 'live' function each 'interval'"
  [actor]
  (do
    (. Thread sleep (interval actor))
    (send-off *agent* loop-actor))
  (live actor))

(defn live-world
  [w]  
  (doseq [actor (:actors w)]
    (send-off actor loop-actor)))
      