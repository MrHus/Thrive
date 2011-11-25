(ns thrive.drawer
  (:use [thrive.core :only (world)])
  (:use seesaw.core)
  (:use seesaw.graphics)
  (:use seesaw.color))
  
(def cell-size 50)

(def tiles 
{
    :sea      {:color (color "blue")},
    :grass    {:color (color "green")},
    :lava     {:color (color "red")},
    :mountain {:color (color "gray")}
})

(defn paint-world 
  "Paints the world.
   c = The canvas on which to paint
   g = The graphics2D context"
  [c g]
  (doseq [cell @world]
    (println "repaint")
    (.setColor g ((tiles (:tile cell)) :color))
    (.fillRect g (* cell-size (:x cell)) (* cell-size (:y cell)) cell-size cell-size)))

(defn make-ui
  [on-close]
  (frame 
    :title "Thrive" 
    :width 500 :height 500 
    :on-close on-close
    :content 
      (border-panel
        :center (canvas :id :canvas,
                        :background "#ffffff", 
                        :paint paint-world))))

(defn add-behaviors
  [root]
  (do
    (timer (fn [_] (repaint! (select root [:#canvas]))) :delay 60 :start? true, :repeats? true)
    root))

(defn app [on-close]
  (invoke-later
    (-> (make-ui on-close)
      add-behaviors 
      show!)))

(defn -main [& args]
  (app :exit))
            