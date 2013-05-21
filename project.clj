(defproject eqalc "0.1.0-SNAPSHOT"
  :description "Equation calculator"
  :url "https://github.com/alexey-naydenov/eqalc"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/math.numeric-tower "0.0.2"]
                 [seesaw "1.4.3"]
                 [instaparse "1.1.0"]]
  :main eqalc.core)
