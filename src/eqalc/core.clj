(ns eqalc.core
  (:gen-class)
  (:use [seesaw core mig])
  (:require [eqalc equations gui]))

(defn -main [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (invoke-later
   (native!)
   (-> (frame :title "Equation Calculator"
              :content "Hello, Seesaw"
              :width 800
              :height 600
              :on-close :exit)
       show!)))

