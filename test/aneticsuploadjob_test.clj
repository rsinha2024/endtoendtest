(ns aneticsuploadjob_test
  (:require
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            [api.aneticsupload_db :as aneticsupload_db]
            [jobs.aneticsuploadjob :as aneticsuploadjob]
            [api.client :as client]
            )
  (:import (java.time LocalDate)
           (java.time.format DateTimeFormatter)))
 (defn get-current-date []
        (let [formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd")
              current-date (LocalDate/now)]
          (.format current-date formatter)))

(def trade_date (get-current-date))
(def steps
       [
        (step :Given "Given records in agent_loan_requests table in status =new and sent_to_anetics !=true transactions"
              (fn given_records_in_agent_loan_requests_table_in_status_new_and_sent_to_anetics_true_transactions [state ]

                (aneticsupload_db/delete-sample-records)
                (aneticsupload_db/insert-records  "end2end" trade_date )))

        (step :Then "Generate a loan transaction file"
              (fn generate_a_loan_transaction_file [state ]

                (aneticsuploadjob/workflow trade_date)))

        (step :Then "Save a copy of the file to S3 bucket env.drivewealth.agentlending\\/yyyymmdd"
              (fn save_a_copy_of_the_file_to_s3_bucket_env_drivewealth_agentlending_yyyymmdd [state ]
                ;; Write code here that turns the phrase above into concrete actions
                state))

        (step :Then "The filename pattern is loan_transactions_yyyymmdd_{int}.csv"
              (fn the_filename_pattern_is_loan_transactions_yyyymmdd_csv [state ^Integer int1]
                (let [job-id (:job-id state)]  ;; Extract job-id from state (or provide it)
                  (println "Job id - " job-id)
                  ;; Poll the job status for up to 20 seconds, checking every 1 second
                  (client/poll-job-status job-id 20000 1000)

                  ;; Retrieve the job status
                  (let [status (client/get-job-status job-id)]

                    ;; Assert that the status is "COMPLETED"
                    (is (= status "COMPLETED") (str "Expected job status to be 'COMPLETED', but was " status))

                    ;; Optionally, you can add more validation here if needed.
                    ))
                ))

        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/aneticsupload.feature" steps))))
