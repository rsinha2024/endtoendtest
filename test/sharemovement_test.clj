(ns sharemovement_test
  (:require [api.dynamodb :as dynamo]
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            ))

  (def steps
       [
        (step :Given "some transactions in agent_loan_requests table in settled status for a given trade date"
              (fn some_transactions_in_agent_loan_requests_table_in_settled_status_for_a_given_trade_date [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (throw (io.cucumber.java.PendingException.))))

        (step :Then "sharemovement file"
              (fn sharemovement_file [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (throw (io.cucumber.java.PendingException.))))

        (step :Then "file name should be in format eg DRVW_INTE_transactions_ordertrans_lending_{int}.txt"
              (fn file_name_should_be_in_format_eg_drvw_inte_transactions_ordertrans_lending_txt [state ^Integer int1]
                ;; Write code here that turns the phrase above into concrete actions
                (throw (io.cucumber.java.PendingException.))))
        ])

  (deftest test-scenario
           (is (zero? (run-cucumber "test/features/sharemovement.feature" steps))))
