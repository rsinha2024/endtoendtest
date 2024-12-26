(ns endtoenedtest.step-definitions.calculator
  (:require [io.cucumber.java.en :refer :all])
  (:import (io.cucumber.java.en Given Then When)))

(defn calculator []
  {:value 0})

(defn add-to-calculator [calc value]
  (assoc calc :value (+ (:value calc) value)))

(defn press-add [calc]
  calc)

(defn result-should-be [calc expected]
  (assert (= (:value calc) expected)))

;; Step Definitions
(Given "I have entered {int} into the calculator"
       [number]
       (def calculator-instance (add-to-calculator (calculator) number)))

(Given "I have entered {int} into the calculator"
       [number]
       (def calculator-instance (add-to-calculator calculator-instance number)))

(When "I press add"
      []
      (press-add calculator-instance))

(Then "the result should be {int}"
      [result]
      (result-should-be calculator-instance result))

