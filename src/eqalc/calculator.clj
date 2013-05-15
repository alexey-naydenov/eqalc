(ns eqalc.calculator)

(defn update-values [vals {:keys [name fun]}]
  (conj vals {name ((eval fun) vals)}))

(defn calculate-equation? [vals {:keys [name]}] (not (contains? vals name)))

(defn calculate [vals eqns]
  (reduce update-values vals (filter (partial calculate-equation? vals) eqns)))



