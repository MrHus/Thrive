(ns thrive.drawer
  (:use [thrive.core :only (world live-world)])
  
  (:require [thrive.human :only (Human)])
  (:import (thrive.human Human))
  
  (:require [thrive.city :only (City)])
  (:import (thrive.city City))
  
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
    :desert   {:color (color "yellow")},
    :unknown  {:color (color "black")}
})

(defn paint-half-block
  "Paint a half block based on the x and y coordinates"
  [g x y block-color]
  (do
    (.setColor g (color block-color))
    (.fillRect g (+ (* cell-size x) (/ cell-half-size 2)) (+ (* cell-size y) (/ cell-half-size 2)) cell-half-size cell-half-size)))

(defn paint-half-circle
  "Paint a half circle based on the x and y coordinates"
  [g x y circle-color]
  (do
    (.setColor g (color circle-color))
    (.fillOval g (+ (* cell-size x) (/ cell-half-size 2)) (+ (* cell-size y) (/ cell-half-size 2)) cell-half-size cell-half-size)))
  
(defn paint-cells
  "Paint cells"
  [c g cells]
  (doseq [cell cells]
    (.setColor g ((tiles (:tile cell)) :color))
    (.fillRect g (* cell-size (:x cell)) (* cell-size (:y cell)) cell-size cell-size)
    (if (= :mountain (:tile cell))
      (do
        (.setColor g (color "black"))
        (.drawString g (str (:z cell)) (+ cell-half-size (* cell-size (:x cell))) (+ cell-half-size (* cell-size(:y cell))))))
    (if (not (zero? (:food cell)))
      (paint-half-block g (:x cell) (:y cell) "orange"))))

(defn paint-world 
  "Paints the world.
   c = The canvas on which to paint
   g = The graphics2D context"
  [c g]
  (do
    (paint-cells c g (:cells @world))
    (doseq [actor (:actors @world)]
      (paint @actor g))))

;; Load helper files
(load "drawer/human")
(load "drawer/city")
       
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

(defn handle-hit-on-actor
  "Handles a hit on the actor. Shows different windows based on actor.
   The actor is a reference (ref) so it needs te be dereferenced in instance?"
  [actor]
  (cond
    (instance? Human @actor) (handle-hit-on-human actor)
    (instance? City @actor)  (handle-hit-on-city actor)))

(defn detect-hit-on-actor
  "If the user clicks on an actor show a nice window with the actors info"
  [^MouseEvent e]
  (let [x (- (. e getX) 3)
        y (- (. e getY) 24)]
    (doseq [actor (:actors @world)]
     (let [ax (+ (* cell-size (:x @actor)) (/ cell-half-size 2)) 
           ay (+ (* cell-size (:y @actor)) (/ cell-half-size 2))]
        (if (and (> x ax) (< x (+ ax cell-half-size)) (> y ay) (< y (+ ay cell-half-size)))
          (handle-hit-on-actor actor))))))
                   
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

(if (= (System/getProperty "os.name") "Windows 7") (-main))