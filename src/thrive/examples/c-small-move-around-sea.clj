(def world-size 3)

(def unknown-world (generate-unknown-world world-size))

(def world (ref
  [(Cell.  0,  0,  0,  :lava, 0)
   (Cell.  1,  0,  0,  :grass,  100)
   (Cell.  2,  0,  0,  :grass,   0)
 
   (Cell.  0,  1,  0,  :lava,  0)
   (Cell.  1,  1,  0,  :sea,  0)
   (Cell.  2,  1,  0,  :forest,  0)
 
   (Cell.  0,  2,  0,  :grass,  0)
   (Cell.  1,  2,  0,  :grass,   0)
   (Cell.  2,  2,  0,  :grass,   0)
]))

(def actors (ref [
  (agent (City. 0 2 0 250 unknown-world)) 
  (agent (Human. 0 2 0 50 unknown-world [0 2] :city [] :mdp))
  (agent (Human. 2 0 0 50 @world [0 2] :city [] :a*))
]))