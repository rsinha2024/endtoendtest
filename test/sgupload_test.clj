(ns sgupload_test
  (:require [api.dynamodb :as dynamo]
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            [api.sgupload_db :as sgupload_db]
            [api.client :as client]
            [jobs.sgupload :as sgupload]))

  (def steps
       [
        (step :Given "some transactions in agent_loan_requests table in settled, rejected or cancelled status"
              (fn some_transactions_in_agent_loan_requests_table_in_settled_rejected_or_cancelled_status [state ]
                (println "In some_transactions_in_agent_loan_requests_table_in_settled_rejected_or_cancelled_status having state " state)
                (let [user_id (dynamo/scan-for-user-id "FREE")]
                (sgupload_db/delete-sample-records)
                (sgupload_db/insert-sample-records user_id "2024-12-27" )
                (let [transactions (sgupload_db/get-transactions-from-db)]
                  (is (pos? (count transactions)) "No transactions found")
                  transactions
                 ))))


        (step :Given "in not sent status"
              (fn in_not_sent_status [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (println "In in_not_sent_status method having state " state)

                ))


        (step :Then "generate loan status file"
              (fn generate_loan_status_file [state ]
                (println "In generate_loan_status_file")
                (sgupload/workflow "2024-12-27" )
                ))

        (step :Then "update sent_to_agent column to Y"
              (fn update_sent_to_agent_column_to_y [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (println "Validating..." state)

                ;; Assuming job-id is part of the state, otherwise you need to extract it
                (let [job-id (:job-id state)]  ;; Extract job-id from state (or provide it)
                  (println "Job id - " job-id)
                  ;; Poll the job status for up to 20 seconds, checking every 1 second
                  (client/poll-job-status job-id 20000 1000)

                  ;; Retrieve the job status
                  (let [status (client/get-job-status job-id)]

                    ;; Assert that the status is "COMPLETED"
                    (is (= status "COMPLETED") (str "Expected job status to be 'COMPLETED', but was " status))

                    ;; Optionally, you can add more validation here if needed.
                    ))))

        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/sgupload.feature" steps))))
