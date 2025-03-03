(ns sgpositionsjob_test
  (:require [api.dynamodb :as dynamo]
            [api.s3 :as s3]
            [api.client :as client]
            [jobs.sgpositionsjob :as sgpositionsjob]
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
        (step :Given "some active loan positions in inteliclear"
              (fn some_active_loan_positions_in_inteliclear [state ]
                ;; Write code here that turns the phrase above into concrete actions
                state))

        (step :Then "fetch the positions from inteliclear"
              (fn fetch_the_positions_from_inteliclear [state ]
                ;; Write code here that turns the phrase above into concrete actions
                (sgpositionsjob/workflow trade_date)))

        (step :Then "and insert them into agent lending database"
              (fn and_insert_them_into_agent_lending_database [state ]
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
           (is (zero? (run-cucumber "test/features/sgpositionsjob.feature" steps))))
