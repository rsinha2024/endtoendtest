(ns daily_accrual_test
  (:require [api.dynamodb :as dynamo]
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            [jobs.daily_accrual :as da]
            ))
  (def trade_date "2024-12-27")
  (def steps
       [(step :Given "A daily accrual file in the format ACCRUALS.DAILY.yyyymmdd.hhmmss.csv"
              (fn a_daily_accrual_file_in_the_format_accruals_daily_yyyymmdd_hhmmss_csv [state ]
                (println "In a_daily_accrual_file_in* method")
                (da/setup trade_date)))

        (step :Given "Uploaded to the s3 folder  S3 bucket that stores the partners sftp files,env.drivewealth.sftp\\/freetrade\\/inbound"
              (fn uploaded_to_the_s3_folder_s3_bucket_that_stores_the_partners_sftp_files_env_drivewealth_sftp_freetrade_inbound [state ]
                ;; Write code here that turns the phrase above into concrete actions
                state
                ))

        (step :Then "The job will parse the file and save the data in agent_lending._daily_accural table"
              (fn the_job_will_parse_the_file_and_save_the_data_in_agent_lending__daily_accural_table [state ]
                ;; Write code here that turns the phrase above into concrete actions
                  (let [{:keys [user_id s3file data] } state
                        ]
                    (println "Triggering daily accrual job")
                   (da/trigger-job user_id trade_date)
                )))

        (step :Then "The job should move the file to archive folder after successful process"
              (fn the_job_should_move_the_file_to_archive_folder_after_successful_process [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (throw (io.cucumber.java.PendingException.))))])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/daily_accrual.feature" steps))))
