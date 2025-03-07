(ns generator_test
  (:require
            [clojure.test :refer [deftest is]]
            [daily_accrual_test :as daily_accrual_test]
            [monthly_accrual_test :as monthly_accrual_test]
            [sgdownload_test :as sgdownload_test]
            [sgupload_test :as sgupload_test]
            [sgpositionsjob_test :as sgpositionsjob_test]
            [loanet_import_file_test :as loanet_import_file_test]
            [sharemovement_test :as sharemovement_test]
            [aneticsuploadjob_test :as aneticsuploadjob_test]
            [aneticsdownload_test :as aneticsdownload_test]
            ))


  (deftest integration-test
         (daily_accrual_test/test-scenario)
         (monthly_accrual_test/test-scenario)
         (sgdownload_test/test-scenario)
         (sgupload_test/test-scenario)
         (sgpositionsjob_test/test-scenario)
         (loanet_import_file_test/test-scenario)
         (sharemovement_test/test-scenario)
         (aneticsuploadjob_test/test-scenario)
         (aneticsdownload_test/test-scenario))
