(ns eqalc.equations)

(def nounit-name "Unit")

(def boost-converter [{:name "V_out" :unit "V" :fun '(fn [vals] 28)}
                      {:name "I_out" :unit "A" :fun '(fn [vals] 0.05)}
                      {:name "f_min" :unit nounit-name :fun '(fn [vals] 50000)}
                      {:name "V_in_min" :unit "V" :fun '(fn [vals] 6.75)}
                      {:name "V_in" :unit "V" :fun '(fn [vals] 9.0)}
                      {:name "V_ripple_pp" :unit nounit-name :fun '(fn [vals] 0.005)}
                      {:name "V_sat" :unit "V" :fun '(fn [vals] 0.3)}
                      {:name "V_F" :unit "V" :fun '(fn [vals] 0.8)}

                      {:name "ratio" :unit nounit-name
                       :fun '(fn [{:strs [V_out V_F V_in_min V_sat]}] 
                               (/ (+ V_out (- V_F V_in_min))
                                  (- V_in_min V_sat)))}

                      {:name "t_off" :unit "s"
                       :fun '(fn [{:strs [f_min ratio]}]
                               (/ 1.0 (* f_min (+ 1.0 ratio))))}
                      {:name "t_on" :unit "s"
                       :fun '(fn [{:strs [ratio t_off]}]
                               (* ratio t_off))}
                      {:name "duty" :unit nounit-name
                       :fun '(fn [{:strs [t_on t_off]}]
                               (/ t_on (+ t_on t_off)))}
                      
                      {:name "C_T" :unit "F"
                       :fun '(fn [{:strs [t_on]}]
                               (* 4.0e-5 t_on))}
                      {:name "I_pk_switch" :unit "A"
                       :fun '(fn [{:strs [I_out ratio]}]
                               (* 2 I_out (+ ratio 1)))}
                      {:name "L_min" :unit "H"
                       :fun '(fn [{:strs [V_in_min V_sat I_pk_switch t_on]}]
                               (* t_on (/ (- V_in_min V_sat) I_pk_switch)))}
                      {:name "R_sc" :unit "Om"
                       :fun '(fn [{:strs [V_in V_sat L_min t_on]}]
                               (/ 0.33 
                                  (* t_on (/ (- V_in V_sat) L_min))))}

                      {:name "C_0" :unit "F"
                       :fun '(fn [{:strs [I_out V_ripple_pp V_out t_on]}]
                               (* t_on (/ I_out (* V_ripple_pp V_out))))}

                      ])

