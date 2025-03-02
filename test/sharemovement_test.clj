(ns sharemovement_test
  (:require [api.dynamodb :as dynamo]
            [api.s3 :as s3]
            [jobs.sharemovement :as sharemovement]
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            )
  (:import (java.time LocalDate)
           (java.time.format DateTimeFormatter)))
 (defn get-current-date []
        (let [formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd")
              current-date (LocalDate/now)]
          (.format current-date formatter)))

(def trade_date "2024-11-13")
(def steps
       [
        (step :Given "some transactions in agent_loan_requests table in settled status for a given trade date"
              (fn some_transactions_in_agent_loan_requests_table_in_settled_status_for_a_given_trade_date [state ]
                (s3/move-s3-files-to-archive "dev.drivewealth.aod" "20250301/ICLEAR_S3/" "20250301/ICLEAR_S3/archive/")
               ))

        (step :Then "sharemovement file"
              (fn sharemovement_file [state ]
                (sharemovement/workflow trade_date)
                ))

        (step :Then "file name should be in format eg DRVW_INTE_transactions_ordertrans_lending_{int}.txt"
              (fn file_name_should_be_in_format_eg_drvw_inte_transactions_ordertrans_lending_txt [state ^Integer int1]
                (println "Verifying output")
                (let [outfile (s3/list-s3-files "dev.drivewealth.aod" "20250301/ICLEAR_S3/")]
                  (println "outfile=" outfile)
                  )
                ))
        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/sharemovement.feature" steps))))
