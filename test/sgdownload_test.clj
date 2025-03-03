(ns sgdownload_test
  (:require [api.dynamodb :as dynamo]
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            [api.sgupload_db :as sgupload_db]
            [api.client :as client]
            [jobs.sgdownload :as sgdownload]))
(def trade_date "2024-10-01")
(def steps
       [
        (step :Given "Default location of the incoming file is $bucket\\/$wlp\\/inbound, can be overwritten using a config table"
              (fn default_location_of_the_incoming_file_is_$bucket_$wlp_inbound_can_be_overwritten_using_a_config_table [state]
                ;; Write code here that turns the phrase above into concrete actions
                ; (sgdownload/setup trade_date )
                ))

        (step :Then "Upload the processed data into the postgres database \\(agent_loan_requests)"
              (fn upload_the_processed_data_into_the_postgres_database_agent_loan_requests [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (let [{:keys [user_id ] } state
                      ]
                  (println "Triggering Patner download job" "user id=" user_id)
                  (sgdownload/workflow trade_date)
                  )))

        (step :Then "Move the file to archive folder once processed"
              (fn move_the_file_to_archive_folder_once_processed [state ]
                ;; Write code here that turns the phrase above into concrete actions
                state))

        (step :Then "Track the files received and processing. \\(agent_load_file_tracking)"
              (fn track_the_files_received_and_processing_agent_load_file_tracking [state]
                ;; Write code here that turns the phrase above into concrete actions
                ;  (sgdownload/validate state trade_date)
                ))

        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/sgdownload.feature" steps))))
