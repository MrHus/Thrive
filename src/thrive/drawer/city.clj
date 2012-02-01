(in-ns 'thrive.drawer)

(extend-type City
  Paintable
  (paint [this g]
    (paint-0-8-block g (:x this) (:y this) "purple")))

;; Will be an atom that points to an agent. Aka a ref to a ref!
(def selected-city (atom nil))

(defn paint-selected-city
  [c g]
  (do
    (paint-cells c g (:world @@selected-city))))

(defn show-city-detail
  []
  (frame
    :title "City detail"
    :width 500 :height 500
    :on-close :hide
    :content
      (border-panel
        :center (canvas 
                  :id :show-city-detail-canvas,
                  :background "#ffffff",
                  :paint paint-selected-city))))

(defn add-behaviors-for-city-window
  [root]
  (let [t (timer (fn [_] (repaint! (select root [:#show-city-detail-canvas]))) :delay 60 :start? true, :repeats? true)]
    (listen root :window-closing (fn [_] (.stop t))))
  root)

(defn handle-hit-on-city
  "Show a nice window with the citys info"
  [^City city]
  (reset! selected-city city)
  (invoke-later
    (-> (show-city-detail)
      add-behaviors-for-city-window 
      show!)))
