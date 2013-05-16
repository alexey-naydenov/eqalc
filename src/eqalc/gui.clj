(ns eqalc.gui
  (:use [seesaw core])
  (:use [seesaw core mig])
  (:use [eqalc calculator])
  (:use [eqalc prefix]))

(defn equation->gui [{:keys [name unit]}]
  {:name-label [name ""]
   :value-show [(text :columns 10 :editable? false :halign :right) ""]
   :value-edit [(text :columns 10 :halign :right) ""]
   :unit-label [unit "left, wrap"]})

(defn guis->items [guis]
  (apply concat (map (fn [m] [(m :name-label) (m :value-edit)
                              (m :value-show) (m :unit-label)]) guis)))

(defn equations->items [eqns]
  (apply concat (map equation->gui eqns)))

(defn show-value [vals el]
  (let [val (double (vals (first (:name-label el))))
        show-el (first (:value-show el))
        unit-el (first (:unit-label el))]
    (text! show-el (pformat val))))

(defn a-calculate [eqns guis e]
  (let [vals (calculate {} eqns)]
    (doall (map (partial show-value vals) guis))))

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
