(ns eqalc.equations)

(def boost-converter [{:name "V_out" :unit "V" :fun '(fn [vals] 28)}
                      {:name "I_out" :unit "A"
                       :fun '(fn [vals] (vals "V_out"))}])

