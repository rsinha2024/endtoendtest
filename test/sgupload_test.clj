(ns sgupload_test
      (:require [clojure.test :refer [deftest is]]
                [burpless :refer [run-cucumber step]]
                [jobs.sgupload :as sgupload]))

  (def steps
       [
        (step :Given "some transactions in agent_loan_requests table in settled, rejected or cancelled status"
              (fn some_transactions_in_agent_loan_requests_table_in_settled_rejected_or_cancelled_status [state ]
                (println "In some_transactions_in_agent_loan_requests_table_in_settled_rejected_or_cancelled_status having state " state)
                 45
               ))


        (step :Given "in not sent status"
              (fn in_not_sent_status [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (println "In in_not_sent_status method having state " state)
                ))


        (step :Then "generate loan status file"
              (fn generate_loan_status_file [state ]
                ;; Write code here that turns the phrase above into concrete actions
                ))

        (step :Then "update sent_to_agent column to Y"
              (fn update_sent_to_agent_column_to_y [state ]
                ;; Write code here that turns the phrase above into concrete actions
                ))

        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/sgupload.feature" steps))))
