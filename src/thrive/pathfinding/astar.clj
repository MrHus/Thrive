(ns thrive.pathfinding.astar)

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

(defn get-cell
  "get the cell out of the world"
  [[x y] world]
  (nth (nth world y) x))

(defn is-walkable-cell?
  "Validite if the x and y coordiante has a walkable cell by the rules of the traversable list"
  [[x y] traversable world]
  (if 
    (or
      (or 
        (or 
          (or (< x 0) (< y 0)) 
          (>= y (count world)))
        (>= x (count (nth world y))))
      (contains? (into {} (filter #(-> % val (= false)) traversable)) (:tile (get-cell [x y] world))))
    false
    true))

(defn is-valid-move?
  "validates if the stap is a validmove "
  [[x1 y1] [x2 y2] movement traversable world]
  )

(defn get-path-a*
  "Calcuate a path from the current point to the finish point on the given world"
  [[x1 y1] [x2 y2] traversable simple-world]
  
  )