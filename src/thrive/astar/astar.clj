(ns thrive.astar.astar)


(defn manhattan-distance 
  "http://en.wikipedia.org/wiki/Taxicab_geometry"
  [[x1 y1] [x2 y2]]
  (+ (Math/abs ^Integer (- x2 x1)) (Math/abs ^Integer (- y2 y1))))

(defn cost 
  "g(x) - cost of getting to that node from starting node. 
    h(x) - cost of getting to the goal node from current node.
    f(x) - g(x)+h(x) "
  [curr start end world]
  (let [g (manhattan-distance start curr) 
        h (manhattan-distance curr end)   
        f (+ g h)]
    [f g h]))

(defn get-path-a*
  "Calcuate a path from the current point to the finish point on the given world"
  [[x1 y1] [x2 y2] traversable simple-world]
  
  )