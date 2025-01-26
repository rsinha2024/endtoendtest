(ns aneticsdownload_test
  (:require [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            [api.client :as client]
            [jobs.aneticsdownload_job :as adj]
            ))
(def trade_date "2025-01-20")
  (def steps
       [

        (step :Given "Records in the database api"
              (fn records_in_the_database_api [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (adj/setup trade_date)
                ))

        (step :Then "Update them based on Anetics response"
              (fn update_them_based_on_anetics_response [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (adj/trigger-job trade_date state)
                ))

        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/aneticsdownload.feature" steps))))
