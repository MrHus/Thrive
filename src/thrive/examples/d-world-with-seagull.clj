(def world-size 3)

(def unknown-world (generate-unknown-world world-size))

(def world (ref
  [(Cell.  0,  0,  0,  :lava,   0)
   (Cell.  1,  0,  0,  :desert, 0)
   (Cell.  2,  0,  0,  :mountain-1, 0)
   
   (Cell.  0,  1,  0,  :sea,   0)
   (Cell.  1,  1,  0,  :grass, 0)
   (Cell.  2,  1,  0,  :forest, 0)
   
   (Cell.  0,  2,  0,  :not-reachable,   0)
   (Cell.  1,  2,  0,  :forest, 50)
   (Cell.  2,  2,  0,  :unknown, 0)]))

(def actors (ref [
      (agent (Seagull. 0 0 0 ))
      (agent (Seagull. 0 0 0 ))
    ]))