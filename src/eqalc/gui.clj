(ns eqalc.gui
  (:use [seesaw core])
  (:use [seesaw core mig])
  (:use [eqalc calculator]))

(defn equation->gui [{:keys [name unit]}]
  {:name-label [name ""]
   :value-edit [(text :columns 20) ""]
   :unit-label [unit "left, wrap"]})

(defn guis->items [guis]
  (apply concat (map (fn [m] [(m :name-label) (m :value-edit) (m :unit-label)])
                     guis)))

(defn equations->items [eqns]
  (apply concat (map equation->gui eqns)))

(defn update-gui-element [vals el]
  (text! (first (:value-edit el)) (vals (first (:name-label el)))))

(defn a-calculate [eqns guis e]
  (let [vals (calculate {} eqns)]
    (doall (map (partial update-gui-element vals) guis))))

(defn a-exit [e] (dispose! e))

(defn equations->panel [eqns]
  (let [value-guis (map equation->gui eqns)
        calculate-button (button :text "Calculate"
                                 :listen [:action 
                                          (partial a-calculate eqns 
                                                   value-guis)])
        quit-button (button :text "Quit" 
                            :listen [:action a-exit])]
  (mig-panel :constraints ["", "[right]"]
             :items (concat (guis->items value-guis)
                            [[:separator "growx, span, wrap, gaptop 10"]
                             [calculate-button "span 2"]
                             [quit-button "wrap"]]))))
