(ns eqalc.calculator
  (:require [instaparse.core :as insta]))

(defn update-values [vals {:keys [name fun]}]
  (conj vals {name ((eval fun) vals)}))

(defn calculate-equation? [vals {:keys [name]}] (not (contains? vals name)))

(defn calculate [vals eqns]
  (reduce update-values vals (filter (partial calculate-equation? vals) eqns)))

(def equation-grammar 
  ["expr = add-sub"
   "<add-sub> = mul-div | add | sub"
   "add = add-sub <'+'> mul-div"
   "sub = add-sub <'-'> mul-div"
   "<mul-div> = term | mul | div"
   "mul = mul-div <'*'> term"
   "div = mul-div <'/'> term"
   "function = id lparen arguments rparen"
   "arguments = args"
   "<args> = add-sub (comma add-sub)* | Epsilon"
   "<term> = id | number | lparen add-sub rparen | function"
   "<comma> = <ws*','ws*>"
   "<lparen> = <ws*'('ws*>"
   "<rparen> = <ws*')'ws*>"
   "id = <ws*> #'[a-zA-Z]+[a-zA-Z0-9_-]*' <ws*>"
   "number = <ws*> #'[0-9]+' <ws*>"
   "ws = #'[\\s\\t]+'"])

(def equation-parser (insta/parser 
                      (apply str (interpose "\n" equation-grammar))))

(def evaluation-transform {:add + :sub - :mul * :div /
                           :number read-string :expr identity})

(defn equation->ast [equation]
  (equation-parser equation))

(defn evaluate-expression [expression-string]
  (->> (equation-parser expression-string) 
       (insta/transform evaluation-transform)))
