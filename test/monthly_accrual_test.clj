(ns monthly_accrual_test
  (:require [api.dynamodb :as dynamo]
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            [jobs.monthly_accrual :as ma]
            ))
  (def billing_start "2024-09-01")
  (def billing_end "2024-09-30")
  (def billing_month "September 2024")

  (def steps
       [
        (step :Given "A monthly accrual file in the format BILLING_ALLOCATION_MONTHLY.yyyymmdd.hhmmss.csv"
              (fn a_monthly_accrual_file_in_the_format_billing_allocation_monthly_yyyymmdd_hhmmss_csv [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (ma/setup billing_month billing_start billing_end)))

        (step :Given "Uploaded to the s3 folder  S3 bucket that stores the partners sftp files,env.drivewealth.sftp\\/freetrade\\/inbound"
              (fn uploaded_to_the_s3_folder_s3_bucket_that_stores_the_partners_sftp_files_env_drivewealth_sftp_freetrade_inbound [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (throw (io.cucumber.java.PendingException.))))

        (step :Then "The job will parse the file and save the data in agent_lending._monthly_accrual table"
              (fn the_job_will_parse_the_file_and_save_the_data_in_agent_lending__monthly_accrual_table [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (throw (io.cucumber.java.PendingException.))))

        (step :Then "The job should move the file to archive folder after successful process"
              (fn the_job_should_move_the_file_to_archive_folder_after_successful_process [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (throw (io.cucumber.java.PendingException.))))
        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/monthly_accrual.feature" steps))))
