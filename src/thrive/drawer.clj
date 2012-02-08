(ns thrive.drawer
  (:use [thrive.core :only (world world-size actors live-world cleanup-dead)])
  (:use [thrive.actor :only (alive?)])
  (:import (java.io BufferedReader FileReader))
  
  (:require [thrive.human :only (Human)])
  (:import (thrive.human Human))
  
  (:require [thrive.city :only (City)])
  (:import (thrive.city City))
  
  (:require [thrive.seagull :only (Seagull)])
  (:import (thrive.seagull Seagull))
  
  (:require [thrive.bear :only (Bear)])
  (:import (thrive.bear Bear))
  
  (:import (java.awt.event MouseEvent))
  
  (:use seesaw.core)
  (:use seesaw.graphics)
  (:use seesaw.color)
  (:gen-class))

(def cell-size 50)
(def cell-half-size (/ cell-size 2))
(def cell-0-8-size (* cell-size 0.8))
(def use-sprites? true)
(if use-sprites?
  (def tile-sprite (javax.imageio.ImageIO/read (clojure.java.io/resource "img/tiles.big.png"))))

(defprotocol Paintable
  "Defines something paintable"
  (paint [this g] "How something gets painted"))


;{:key [x y width height]}
(def tiles 
  {
   :sea           {:color (color "blue")      :sprite [(* 36 3) 0 36 36]}
   :forest        {:color (color "darkgreen") :sprite [(* 36 4) 0 36 36]},
   :grass         {:color (color "green")     :sprite [(* 36 0) 0 36 36]},
   :lava          {:color (color "red")       :sprite [(* 36 2) 0 36 36]},
   :mountain-1    {:color (color "gray")      :sprite [(* 36 6) 0 36 36]},
   :mountain-2    {:color (color "gray")      :sprite [(* 36 6) 0 36 36]},
   :mountain-3    {:color (color "gray")      :sprite [(* 36 6) 0 36 36]},
   :desert        {:color (color "yellow")    :sprite [(* 36 1) 0 36 36]},
   :not-reachable {:color (color "honeydew")  :sprite [(* 36 13) 0 36 36]},
   :unknown       {:color (color "black")     :sprite [(* 36 14) 0 36 36]}
   })


(defn get-tile 
  [tile-sprite [x y width height]]
  (.getSubimage tile-sprite x y width height))

(defn paint-half-block
  "Paint a half block based on the x and y coordinates"
  [g x y block-color image-location]
  (if use-sprites?
    (.drawImage g (get-tile tile-sprite image-location) (-(* cell-size x)  3) (- (* cell-size y) 3) (+ 6 cell-size) (+ 6 cell-size) nil)
    (do
      (.setColor g (color block-color))
      (.fillRect g (+ (* cell-size x) (/ cell-half-size 2)) (+ (* cell-size y) (/ cell-half-size 2)) cell-half-size cell-half-size))))

(defn paint-0-8-block
  "Paint a 0.8 block based on the x and y coordinates"
  [g x y block-color image-location]
  (if use-sprites?
    (.drawImage g (get-tile tile-sprite image-location) (-(* cell-size x)  3) (- (* cell-size y) 3) (+ 6 cell-size) (+ 6 cell-size) nil)
    (do
      (.setColor g (color block-color))
      (.fillRect g (+ (* cell-size x) (/ (- cell-size cell-0-8-size) 2)) (+ (* cell-size y) (/ (- cell-size cell-0-8-size) 2)) cell-0-8-size cell-0-8-size))))

(defn paint-half-circle
  "Paint a half circle based on the x and y coordinates"
  [g x y circle-color image-location]
  (if use-sprites?
    (.drawImage g (get-tile tile-sprite image-location) (-(* cell-size x)  3) (- (* cell-size y) 3) (+ 6 cell-size) (+ 6 cell-size) nil)
    (do
      (.setColor g (color circle-color))
      (.fillOval g (+ (* cell-size x) (/ cell-half-size 2)) (+ (* cell-size y) (/ cell-half-size 2)) cell-half-size cell-half-size))))

(defn paint-cells
  "Paint cells"
  [c g cells]
  (doseq [cell cells]    
    (let [coordinates ((tiles (:tile cell)) :sprite)]
      (if (and use-sprites? (not (nil? coordinates)))
         (.drawImage g (get-tile tile-sprite coordinates) (-(* cell-size (:x cell))  3) (- (* cell-size (:y cell)) 3) (+ 6 cell-size) (+ 6 cell-size) nil)
        (do
          (.setColor g ((tiles (:tile cell)) :color))
          (.fillRect g (* cell-size (:x cell)) (* cell-size (:y cell)) cell-size cell-size))))
    (if (= :mountain (:tile cell))
      (do
        (.setColor g (color "black"))
        (.drawString g (str (:z cell)) (+ cell-half-size (* cell-size (:x cell))) (+ cell-half-size (* cell-size(:y cell))))))
    (if (not (zero? (:food cell)))
       (paint-half-block g (:x cell) (:y cell) "orange" [(* 36 11) 0 36 36]))))

(defn paint-world 
  "Paints the world.
   c = The canvas on which to paint
   g = The graphics2D context"
  [c g]
  (do
    (paint-cells c g @world)
    (doseq [actor @actors]
      (paint @actor g))))

;; Load helper files
(load "drawer/human")
(load "drawer/city")

(extend-type Seagull
  Paintable
  (paint [this g]
    (paint-half-block g (:x this) (:y this) "white" [(* 8 36) 0 36 36])))

(extend-type Bear
  Paintable
  (paint [this g]
         (paint-half-block g (:x this) (:y this) "saddlebrown" [(* 7 36) 0 36 36])))

(defn make-ui
  [on-close]
  (frame 
    :title "Thrive" 
    :width (+ (* world-size cell-size) 15) :height (+ (* world-size cell-size) 39)
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
    (doseq [actor @actors]
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