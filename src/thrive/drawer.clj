(ns thrive.drawer
  (:use [thrive.core :only (world live-world)])
  (:require [thrive.human :only (Human)])
  (:import (thrive.human Human))
  (:import (java.awt.event MouseEvent))
  (:use seesaw.core)
  (:use seesaw.graphics)
  (:use seesaw.color)
  (:gen-class))
  
(def cell-size 50)
(def cell-half-size (/ cell-size 2))

(defprotocol Paintable
  "Defines something paintable"
  (paint [this g] "How something gets painted"))

(def tiles 
{
    :sea      {:color (color "blue")},
    :grass    {:color (color "green")},
    :lava     {:color (color "red")},
    :mountain {:color (color "gray")},
    :unknown  {:color (color "black")}
})

(extend-type Human
  Paintable
  (paint [this g]
    (do
      (.setColor g (color "pink"))
      (.fillRect g (+ (* cell-size (:x this)) (/ cell-half-size 2)) (+ (* cell-size (:y this)) (/ cell-half-size 2)) cell-half-size cell-half-size))))

(defn paint-cells
  "Paint cells"
  [c g cells]
  (doseq [cell cells]
    (.setColor g ((tiles (:tile cell)) :color))
    (.fillRect g (* cell-size (:x cell)) (* cell-size (:y cell)) cell-size cell-size)))

(defn paint-world 
  "Paints the world.
   c = The canvas on which to paint
   g = The graphics2D context"
  [c g]
  (do
    (paint-cells c g (:cells @world))
    (doseq [actor (:actors @world)]
      (paint @actor g))))
       
(defn make-ui
  [on-close]
  (frame 
    :title "Thrive" 
    :width 500 :height 500 
    :on-close on-close
    :content 
      (border-panel
        :center (canvas :id :world-canvas,
                        :background "#ffffff", 
                        :paint paint-world))))

;; Will be an atom that points to an agent. Aka a ref to a ref!
(def selected-actor (atom nil))

(defn paint-selected-actor
  [c g]
  (do
    (paint-cells c g (:world @@selected-actor))))

(defn show-actor-detail
  []
  (frame
    :title "Actor detail"
    :width 500 :height 500
    :on-close :hide
    :content
      (border-panel
        :center (canvas :id :show-actor-detail-canvas,
                        :background "#ffffff",
                        :paint paint-selected-actor))))

(defn add-behaviors-for-actor-window
  [root]
  (let [t (timer (fn [_] (repaint! (select root [:#show-actor-detail-canvas]))) :delay 60 :start? true, :repeats? true)]
    (listen root   :window-closing (fn [_] (.stop t))))
  root)

(defn detect-hit-on-actor
  "If the user clicks on an actor show a nice window with the actors info"
  [^MouseEvent e]
  (let [x (- (. e getX) 3)
        y (- (. e getY) 24)]
    (doseq [actor (:actors @world)]
     (let [ax (+ (* cell-size (:x @actor)) (/ cell-half-size 2)) 
           ay (+ (* cell-size (:y @actor)) (/ cell-half-size 2))]
        (if (and (> x ax) (< x (+ ax cell-half-size)) (> y ay) (< y (+ ay cell-half-size)))
          (do
            (reset! selected-actor actor)
            (invoke-later
              (-> (show-actor-detail)
                add-behaviors-for-actor-window 
                show!))))))))
                     
(defn add-behaviors
  [root]
  (let [t (timer (fn [_] (repaint! (select root [:#world-canvas]))) :delay 60 :start? true, :repeats? true)]
    
    (listen root   :window-closing (fn [_] (.stop t)))
    (listen root   :mouse-clicked  detect-hit-on-actor))
  root)

(defn app 
  [on-close]
  (invoke-later
    (-> (make-ui on-close)
      add-behaviors 
      show!)))

(defn -main 
  [& args]
  (do
    (live-world)
    (app :exit)))

(-main)
            