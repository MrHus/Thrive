(def world-size 4)

(def unknown-world (generate-unknown-world world-size))

(def world (ref
  [(Cell.  0,  0,  0,  :grass,  0)
   (Cell.  1,  0,  0,  :grass,  0)
   (Cell.  2,  0,  0,  :grass,  0)
   (Cell.  3,  0,  0,  :forest,  0)
 
   (Cell.  0,  1,  0,  :grass,   0)
   (Cell.  1,  1,  0,  :grass,   0)
   (Cell.  2,  1,  0,  :lava,   0)
   (Cell.  3,  1,  0,  :grass, 0)
 
   (Cell.  0,  2,  0,  :grass,  0)
   (Cell.  1,  2,  0,  :lava,  0)
   (Cell.  2,  2,  0,  :grass,  100)
   (Cell.  3,  2,  0,  :grass,  0)
 
   (Cell.  0,  3,  0,  :desert,  0)
   (Cell.  1,  3,  0,  :grass,  0)
   (Cell.  2,  3,  0,  :grass,  0)
   (Cell.  3,  3,  0,  :grass,  0)
]))

(def actors (ref [
  (agent (City. 0 0 0 250 unknown-world)) 
  (agent (Human. 3 3 0 50 unknown-world [0 0] :city [] :a*))
]))
