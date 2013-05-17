(ns eqalc.prefix-test
  (:require [clojure.test :refer :all]
            [eqalc.prefix :refer :all]))

(deftest power-test
  (testing "multiplier"
    (is (= 1 1))
    (is (= (redable-power 1.0) 0))
    (is (= (redable-power 900.0) 0))
    (is (= (redable-power 1000.0) -3))
    (is (= (redable-power 1000.1) -3))
    (is (= (redable-power 2e6) -6))
    (is (= (redable-power 0.1) 3))
    (is (= (redable-power 1e-7) 9))
    ))
