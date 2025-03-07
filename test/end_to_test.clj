(ns end_to_test
  (:require
            [clojure.test :refer [deftest is]]
            )

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/aneticsupload.feature" steps)))))
