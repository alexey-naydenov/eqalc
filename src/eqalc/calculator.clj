(ns eqalc.calculator
  (:require [instaparse.core :as insta])
  (:require [clojure.math.numeric-tower :as math])
  (:use eqalc.prefix))

(def nounit "")

(defn update-values [vals {:keys [name fun]}]
  ;; Unconditionally update values in dictionary
  (conj vals {name ((eval fun) vals)}))

(defn calculate-equation? [vals {:keys [name]}] 
  ;; Dont update value if it is already in the dict.
  (not (contains? vals name)))

(defn calculate [vals eqns]
  ;; Update values based on equations, dont update set values.
  (reduce update-values vals (filter (partial calculate-equation? vals) eqns)))

(def equation-grammar
  ;; Grammar for equations of the form a = b + c, m V \n
  ["<program> = (assignment <nl>+)* (assignment)?"
   "assignment = varid <'='> add-sub (comma ((prefix unit) | unit))?"
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
   "unit = <ws*> #'[a-zA-Z]+' <ws*>"
   "prefix = <ws*> #'[pnumkMGT]' <ws*>"
   "varid = <ws*> #'[a-zA-Z]+[a-zA-Z0-9_-]*' <ws*>"
   "id = <ws*> #'[a-zA-Z]+[a-zA-Z0-9_-]*' <ws*>"
   "number = <ws*> #'[\\+\\-]?[0-9]*(\\.[0-9]*)?+([eE][\\+\\-]?[0-9]+)?' <ws*>"
   "nl = #'\r?\n'"
   "ws = #'[\\s\\t]+'"])

;; Parser object for the grammar.
(def equation-parser (insta/parser 
                      (apply str (interpose "\n" equation-grammar))))

(defn equations->ast [equation]
  ;; Transform string with equations into an ast.
  (equation-parser equation))

(defn id->vals-read [id]
  ;; Convert id to call to val dictionary call.
  (conj (list id) 'vals))

(defn op->calc [op x y]
  ;; Convert an operator to list with plus operator.
  (list op x y))

(defn assignment->description
  ;; Converts parsed assigment into dictionary with equiation description.
  ([vname vfun]
     (assignment->description vname vfun "" nounit))
  ([vname vfun vunit]
     (assignment->description vname vfun "" vunit))
  ([vname vfun vpref vunit] 
     (let [pfun (if (empty? vpref) 
                  vfun
                  (list '* (list 'math/expt 10 (prefix-power-map vpref)) vfun))] 
       {:name vname :unit vunit
        :fun (list 'fn '[vals] pfun)})))

(defn ast->descriptions [eqns]
  ;; Convert an ast into a vector of dictionaries that allow evaluation.
  ;; Ex: (->> (equations->ast "c = 1e3 + (a + b) , mV\n") ast->descriptions)
  (insta/transform {:varid identity :unit identity :number read-string
                    :prefix identity :id id->vals-read
                    :add (partial op->calc '+) :sub (partial op->calc '-)
                    :mul (partial op->calc '*) :div (partial op->calc '/)
                    :assignment assignment->description}
                   eqns))

(defn equations->descriptions [eqns-string]
  (ast->descriptions (equations->ast eqns-string)))

(def evaluation-transform {:add + :sub - :mul * :div /
                           :number read-string :expr identity})

(defn evaluate-expression [expression-string]
  (->> (equation-parser expression-string) 
       (insta/transform evaluation-transform)))

