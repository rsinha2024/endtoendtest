(ns sgdownload_bad_file_test
  (:require
            [clojure.test :refer [deftest is]]
            [burpless :refer [run-cucumber step]]
            ))
(def trade_date "2024-10-01")
(def steps
  [

   (step :Given "A bad request to SG download job and missing partner field"
         (fn a_bad_request_to_sg_download_job_and_missing_partner_field [state]
           ;; Write code here that turns the phrase above into concrete actions
           (throw (io.cucumber.java.PendingException.))))

   (step :Then "The job should fail"
         (fn the_job_should_fail [state]
           ;; Write code here that turns the phrase above into concrete actions
           (throw (io.cucumber.java.PendingException.))))
   ])

(deftest test-scenario
  (is (zero? (run-cucumber "test/features/sgdownload_file_bad.feature" steps))))
