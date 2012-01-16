(ns thrive.multiearray.multiearray)

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

(defn in? 
  "true if seq contains elm"
  [seq elm]  
  (if (some #(= elm (val %)) seq)
    true
    false))

(defn is-valid-move?
  "validates if the stap is a validmove "
  ([[x1 y1] [x2 y2] movement traversable world]
    (if
      (and 
        (in? movement [(- x2 x1) (- y2 y1)])
        (is-walkable-cell? [x2 y2] traversable world))
      true
      false)))