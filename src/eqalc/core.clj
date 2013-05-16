(ns eqalc.core
  (:gen-class)
  (:use [seesaw core mig])
  (:use [eqalc gui])
  (:use [eqalc equations]))

(defn -main [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (invoke-later
   (native!)
   (-> (frame :title "Equation Calculator"
              :content (equations->panel boost-converter)
              :on-close :exit)
       pack!
       show!)))

