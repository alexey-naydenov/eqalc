(ns eqalc.core
  (:gen-class)
  (:use [seesaw core mig]))

(defn -main [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (invoke-later
   (-> (frame :title "Hello"
              :content "Hello, Seesaw"
              :width 800
              :height 600
              :on-close :exit)
       show!)))
