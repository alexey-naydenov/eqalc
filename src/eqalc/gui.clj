(ns eqalc.gui
  (:use [seesaw core mig]))

(defn equation->gui [{:keys [name unit]}]
  {:name-label [name ""]
   :value-edit [(text :columns 10) ""]
   :unit-label [unit "left, wrap"]})

(defn guis->items [guis]
  (apply concat (map (fn [m] [(m :name-label) (m :value-edit) (m :unit-label)])
                     guis)))

(defn equations->items [eqns]
  (apply concat (map equation->gui eqns)))

(defn a-calculate [e] (alert "Calculate"))

(defn a-exit [e] (dispose! e))

(defn equations->panel [eqns]
  (let [value-guis (map equation->gui eqns)
        
        calculate-button (button :text "Calculate"
                                 :listen [:action a-calculate])
        quit-button (button :text "Quit" 
                            :listen [:action a-exit])]
  (mig-panel :constraints ["", "[right]"]
             :items (concat (guis->items value-guis)
                            [[:separator "growx, span, wrap, gaptop 10"]
                             [calculate-button "span 2"]
                             [quit-button ""]]))))
