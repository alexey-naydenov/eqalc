(ns eqalc.gui
  (:use [seesaw core mig]))

(defn equation->gui [{:keys [name unit]}]
  [[name ""]
   [(text :columns 10) ""]
   [unit "left, wrap"]])

(defn equations->items [eqns]
  (apply concat (map equation->gui eqns)))

(defn equations->panel [eqns]
  (mig-panel :constraints ["", "[right]"]
             :items (concat (equations->items eqns)
                            [[:separator "growx, span, wrap, gaptop 10"]])))
