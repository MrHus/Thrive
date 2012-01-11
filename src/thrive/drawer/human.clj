(in-ns 'thrive.drawer)

(extend-type Human
  Paintable
  (paint [this g]
    (paint-half-block g (:x this) (:y this) "pink")))

;; Will be an atom that points to an agent. Aka a ref to a ref!
(def selected-human (atom nil))

(defn paint-selected-human
  [c g]
  (let [human @@selected-human]
    (paint-cells c g (:world human))
    (paint-half-block g (:x human) (:y human) "pink")
    (paint-half-block g (first (:city human)) (last (:city human)) "purple")))
    
(defn show-human-detail
  []
  (frame
    :title "Human detail"
    :width 500 :height 500
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
