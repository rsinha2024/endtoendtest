(ns loanet_import_file_test
  (:require [api.dynamodb :as dynamo]
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            [jobs.loanet_import_file :as lif]
            ))
  (def trade_date "2024-10-23")
  (def steps
       [(step :Given "Loanet file pao3360 in sftp server folder \\/LOANET\\/EOD"
              (fn loanet_file_pao3360_in_sftp_server_folder_loanet_eod [state ]
                (lif/setup trade_date)
                ))

        (step :Then "Upload the processed data into the postgres database \\(loanet_open_loans)"
              (fn upload_the_processed_data_into_the_postgres_database_loanet_open_loans [state ]
                (lif/workflow trade_date)
                ))])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/loanet.feature" steps))))
