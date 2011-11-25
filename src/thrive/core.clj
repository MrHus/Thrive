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
  (live [this] "The live method"))
  
;;;;;;; References ;;;;;;;
  
(def world (ref [(Cell. 0 0 0 :sea 0), (Cell. 1 0 0 :grass 0), (Cell. 2 0 0 :grass 0), 
                 (Cell. 0 1 0 :sea 0), (Cell. 1 1 0 :sea 0), (Cell. 2 1 0 :mountain 0),
                 (Cell. 0 2 0 :sea 0), (Cell. 1 2 0 :mountain 0), (Cell. 2 2 0 :lava 0)]))  