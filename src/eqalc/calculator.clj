(ns eqalc.calculator
  (:require [instaparse.core :as insta]))

(defn update-values [vals {:keys [name fun]}]
  (conj vals {name ((eval fun) vals)}))

(defn calculate-equation? [vals {:keys [name]}] (not (contains? vals name)))

(defn calculate [vals eqns]
  (reduce update-values vals (filter (partial calculate-equation? vals) eqns)))

(def arithmetic
  (insta/parser
   "expr = add-sub
    <add-sub> = mul-div | add | sub
    add = add-sub <'+'> mul-div
    sub = add-sub <'-'> mul-div
    <mul-div> = term | mul | div
    mul = mul-div <'*'> term
    div = mul-div <'/'> term
    <term> = number | <'('> add-sub <')'>
    number = #'[0-9]+'"))

(def evaluation-transform {:add + :sub - :mul * :div /
                           :number read-string :expr identity})

(defn evaluate-expression [expression-string]
  (->> (arithmetic expression-string) (insta/transform evaluation-transform)))
