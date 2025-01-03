(ns sgupload_test
      (:require [clojure.test :refer [deftest is]]
                [burpless :refer [run-cucumber step]]))

  (def steps
       [
        (step :Given "have some more"
              (fn have_some1[state] (println "Given step blahhahahah") state))

        (step :Given "not sent"
              (fn not_sent[state ] (println "And step") state))

        (step :Then "file gen"
              (fn file_gen[state] (println "Then step") state))
        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/sgupload.feature" steps))))
