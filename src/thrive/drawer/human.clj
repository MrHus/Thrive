(in-ns 'thrive.drawer)

(extend-type Human
  Paintable
  (paint [this g]
    (paint-half-block g (:x this) (:y this) "pink" [(* 9 36) 0 36 36])))

;; Will be an atom that points to an agent. Aka a ref to a ref!
(def selected-human (atom nil))

(defn paint-plan-human
  [g ^Human h]
  (doseq [[x y] (:movement h)]
   (paint-half-circle g x y "cyan")))

(defn paint-selected-human
  [c g]
  (let [human @@selected-human]
    (paint-cells c g (:world human))
    (paint-half-block g (:x human) (:y human) "pink")
    (paint-plan-human g human)
    (paint-0-8-block g (first (:city human)) (last (:city human)) "purple" [(* 9 36) 0 36 36])))
  
(defn show-human-detail
  []
  (frame
    :title "Human detail"
    :width 515 :height 539
    :on-close :hide
    :content
      (border-panel
        :center (canvas 
                  :id :show-human-detail-canvas,
                  :background "#ffffff",
                  :paint paint-selected-human))))

(defn add-behaviors-for-human-window
  [root]
  (let [t (timer (fn [_] (repaint! (select root [:#show-human-detail-canvas]))) :delay 60 :start? true, :repeats? true)]
    (listen root :window-closing (fn [_] (.stop t))))
  root)

(defn handle-hit-on-human
  "Show a nice window with the humans info"
  [^Human human]
  (reset! selected-human human)
  (invoke-later
    (-> (show-human-detail)
      add-behaviors-for-human-window 
      show!)))
