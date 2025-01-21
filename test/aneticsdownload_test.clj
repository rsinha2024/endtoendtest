(ns aneticsdownload_test
  (:require [api.dynamodb :as dynamo]
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            [api.sgupload_db :as sgupload_db]
            [api.client :as client]
            [jobs.sgupload :as sgupload]))

  (def steps
       [
        (step :Given "Default location of the incoming file is $bucket\\/$wlp\\/inbound, can be overwritten using a config table"
              (fn default_location_of_the_incoming_file_is_$bucket_$wlp_inbound_can_be_overwritten_using_a_config_table [state ]

                ))

        (step :Then "Upload the processed data into the postgres database \\(agent_loan_requests)"
              (fn upload_the_processed_data_into_the_postgres_database_agent_loan_requests [state ]

                ))

        (step :Then "Move the file to archive folder once processed"
              (fn move_the_file_to_archive_folder_once_processed [state ]

                ))

        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/aneticsdownload.feature" steps))))
