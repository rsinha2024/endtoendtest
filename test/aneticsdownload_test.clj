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

                (adj/setup trade_date)
                ))

        (step :Then "Update them based on Anetics response"
              (fn update_them_based_on_anetics_response [state ]
                (let [{:keys [user_id s3file data] } state
                      ]
                  (println "Triggering Anetics Download API job")
                (adj/trigger-job trade_date user_id)
                )))

        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/aneticsdownload.feature" steps))))
