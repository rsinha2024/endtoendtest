(ns endtoenedtest.core
  (:require [api.client :as api]   ;; Import the API client module
            [cheshire.core :as cheshire]
            [api.db :as db]
            [api.sgupload_db :as sgupload_db]
            [api.accrual_db :as accrual_db]
            [dag.trigger :as dag]
            [api.s3 :as s3]
            [api.dynamodb :as dynamo]
            [jobs.sgdownload :as sgdownload]
            [jobs.sgupload :as sgupload]
            [file.generator.trade-continous-map :as sgdownloadgen]
            [file.generator.daily-accrual :as dafilegen]
            [api.sftp_client :as sftp]
            [clojure.java.io :as io]
            [jobs.aneticsdownload_job :as aneticsdownloadjob]
            )
  (:gen-class))


(defn -main
  "EndtoEnd tests."
  [& args]
  (println "End to end started!")
  (println "s3" (s3/move-s3-files-to-archive "dev.drivewealth.aod" "20250301/ICLEAR_S3/" "20250301/ICLEAR_S3/archive/"))
  ; (aneticsdownloadjob/workflow "2025-01-20")
  ;(println (resolve 'org.apache.sshd.client.SSHClient))
  ;(sftp/upload_file)
  ;(dafilegen/generate_file "2024-12-27")
  ;(sgupload_db/delete-sample-records)
  ;(sgupload_db/insert-sample-records "07baca37-5612-4ec6-ae8d-a03f12bd3ff53232" "2024-12-27" )
  ;(sgupload_db/get-transactions-from-db)
  ; (dag/extract-dag-run-details "Agent_Lending_Processing_Pipeline")
  ; (process-trade)
  ;(db/query-db)
  ;(db/insert_into_bod_positions1)
  ; (s3/upload-test-file)
  ; (sgdownload/workflow "2024-12-27")
  ;(sgupload/workflow "2024-12-27")
  ;(println (api/get-job-status 10521))
  ;(println (db/key-exists? "07baca37-5612-4ec6-ae8d-a03f12bd3ff53232"))
  ;(println (dynamo/scan-for-user-id "FREE"))
  ;(println (sgdownloadgen/generate_file "./resources/downloads/sgdownloadjob/input.json" "20241224.113937") )
  (println "End to end done!")
  )