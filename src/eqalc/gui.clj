(ns eqalc.gui
  (:require [clojure.string :as string])
  (:use [seesaw core])
  (:use [seesaw core mig])
  (:use [seesaw chooser])
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

(defn read-values [ews]
  ;; Create values map from a vector of maps that contain name and widget.
  (reduce (fn [res {:keys [name value-edit]}]
            (let [value (string/trim (text value-edit))]
              (if-not (empty? value)
                (assoc res name (pscan value))
                res))) {} ews))

(defn a-calculate [ews e]
  ;; Take a list of equations with widgets then calculate and update widgets.
  (try
    (let [vals (calculate (read-values ews) ews)]
      (doall (map (partial update-widget vals) ews)))
    (catch NumberFormatException e (alert (.getMessage e)))))

(declare equations->panel)
(defn a-open [e]
  ;; Open file with equation descriptions.
  (when-let [file-name (choose-file (to-root e))]
    (-> (to-root e) 
        (config! :content (equations->panel 
                           (equations->descriptions (slurp file-name))))
        pack!)))

(def open-action (action :handler a-open
                         :name "Open"
                         :tip "Open equation descriptions"))

(defn equations->panel [eqns]
  ;; Create mig panel based on a list of equation descriptions.
  (let [eqns-widgets (map add-widgets eqns)
        evaluate-action (action :name "Evaluate" 
                                :tip "Evaluate all equations"
                                :handler (partial a-calculate eqns-widgets))
        quit-action (action :name "Quit" :tip "Quit program"
                            :handler (fn [e] (dispose! e)))]
    (a-calculate eqns-widgets {})
    (mig-panel :constraints ["", "[right]"]
               :items (concat [[(toolbar :items [open-action evaluate-action
                                                 :separator quit-action]) 
                                "growx, span, wrap"]]
                              (widgets->items eqns-widgets)))))
