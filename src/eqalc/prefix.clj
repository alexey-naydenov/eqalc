(ns eqalc.prefix
  (:use [clojure set])
  (:require [clojure.math.numeric-tower :as math]))

(defn redable-power
  ;; Calculate multiplier that ensures nice redability of the value.
  ([val] (redable-power val 0))
  ([val rpow] (if (<= 1000.0 val)
               (recur (/ val 1000.0) (+ rpow 3))
               (if (< val 1.0)
                 (recur (* val 1000.0) (- rpow 3))
                 rpow))))

(def power-prefix-map {-12 "p" -9 "n" -6 "u" -3 "m" 0 ""
                       3 "k" 6 "M" 9 "G" 12 "T"})
(def prefix-power-map (map-invert power-prefix-map))

(defn power->prefix [rpow]
  ;; Find metric prefix for given redable power.
  (power-prefix-map rpow))

(defn pformat [val]
  ;; Convert numeric value into a nice looking string with unit prefix.
  (let [dval (double val)
        rpow (redable-power dval)]
    (format "%.2f %s" (/ val (math/expt 10.0 rpow)) (power->prefix rpow))))

(def prefixed-pattern #"\s*([+\-0-9.]+)\s*([pnumkMGT]?)\s*")

(defn pscan [num-string]
  (if-let [[whole-str num prefix] (re-matches prefixed-pattern num-string)]
    (* (read-string num) (math/expt 10.0 (prefix-power-map prefix)))
    (throw (NumberFormatException. 
            (str  "Invalid prefix: " num-string)))))
