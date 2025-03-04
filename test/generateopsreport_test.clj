(ns generateopsreport_test
  (:require [api.dynamodb :as dynamo]
            [api.s3 :as s3]
            [api.client :as client]
            [util.properties :as p]
            [api.generateopsreport_db :as generateopsreport_db]
            [jobs.generateopsreportjob :as generateopsreportjob]
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
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
        (step :Given "Some newly added loan recall transactions in agent lending database."
              (fn some_newly_added_loan_recall_transactions_in_agent_lending_database [state ]
                (generateopsreport_db/delete-sample-records)
                (generateopsreport_db/insert-records  "end2end" trade_date )))

        (step :Given "query condition is trade_type=loan or recall, status=new, sent_to_intc != true, trade_date=$trade_date"
              (fn query_condition_is_trade_type_loan_or_recall_status_new_sent_to_intc_true_trade_date_$trade_date [state ]
                (generateopsreportjob/workflow trade_date)
                ))

        (step :Given "sum\\(quantity) group by cusip, trade_type"
              (fn sum_quantity_group_by_cusip_trade_type [state ]
                state
                ))

        (step :Then "The job produce a report of above transactions,"
              (fn the_job_produce_a_report_of_above_transactions [state ]
                state
                ))

        (step :Then "The report should not include transactions that has been sent in previous run."
              (fn the_report_should_not_include_transactions_that_has_been_sent_in_previous_run [state ]
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
           (is (zero? (run-cucumber "test/features/generateopsreportjob.feature" steps))))
