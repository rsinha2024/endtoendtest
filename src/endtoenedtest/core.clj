(ns endtoenedtest.core
  (:require [api.client :as api]   ;; Import the API client module
            [cheshire.core :as cheshire]
            [api.db :as db]
            [api.s3 :as s3]
            [api.dynamodb :as dynamo]
            [jobs.sgdownload :as sgdownload]
            [file.generator.trade-continous-map :as sgdownloadgen]
            )
  (:gen-class))


(defn -main
  "EndtoEnd tests."
  [& args]
  (println "End to end started!")
  ; (process-trade)
  ;(db/query-db)
  ;(db/insert_into_bod_positions1)
  ;(s3/upload-test-file)
  (sgdownload/workflow "2024-12-27")
  ;(println (db/key-exists? "07baca37-5612-4ec6-ae8d-a03f12bd3ff53232"))
  ;(println (dynamo/scan-for-user-id "FREE"))
  ;(println (sgdownloadgen/generate_file "./resources/downloads/sgdownloadjob/input.json" "20241224.113937") )
  (println "End to end done!")
  )