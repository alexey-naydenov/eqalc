(ns eqalc.gui
  (:use [seesaw core])
  (:use [seesaw core mig])
  (:use [eqalc calculator])
  (:use [eqalc prefix]))

(defn add-widgets [{:keys [name unit] :as eq}] 
  ;; Add widgets to an equation description.
  (merge eq 
         {:name-label name :unit-label unit
          :value-display (text :columns 10 :editable? false :halign :right)
          :value-edit (text :columns 10 :halign :right)}))

(defn widgets->items [ws]
  ;; Convert a list of maps of widgets into a list of items for mig-panel.
  (apply concat (map (fn [m] [[(m :name-label) ""] 
                              [(m :value-edit) ""]
                              [(m :value-display) ""]
                              [(m :unit-label) "left, wrap"]]) ws)))

(defn update-widget [vals {:keys [value-display name]}]
  ;; Update widget text based on values.
  (text! value-display (pformat (vals name))))

(defn a-calculate [ews e]
  ;; Take a list of equations with widgets then calculate and update widgets.
  (let [vals (calculate {} ews)]
    (doall (map (partial update-widget vals) ews))))

(defn a-exit [e] (dispose! e))

(defn equations->panel [eqns]
  ;; Create mig panel based on a list of equation descriptions.
  (let [eqns-widgets (map add-widgets eqns)
        calculate-button (button :text "Calculate"
                                 :listen [:action 
                                          (partial a-calculate eqns-widgets)])
        quit-button (button :text "Quit" :listen [:action a-exit])]
  (mig-panel :constraints ["", "[right]"]
             :items (concat (widgets->items eqns-widgets)
                            [[:separator "growx, span, wrap, gaptop 10"]
                             [calculate-button "span 2"]
                             [quit-button "wrap"]]))))
