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
(def body {:partners ["free"]
           :tradeDate "2024-12-24"})

;; Function to process the request and handle the response
(defn process-trade []
  (let [response (api/send-post-request body)]  ;; Call the send-post-request function from the api.client module
    (if (= 200 (:status response))  ;; Check if the request was successful (status 200)
      (do
        (println "Request succeeded!")
        (println "Response body:" (api/parse-response response)))  ;; Parse and print the response body
      (println "Request failed with status:" (:status response)))))

(defn -main
  "EndtoEnd tests."
  [& args]
  (println "End to end started!")
  ; (process-trade)
  ;(db/query-db)
  ;(db/insert_into_bod_positions1)
  ;(s3/upload-test-file)
   (sgdownload/setup "2024-12-24")
  ;(println (db/key-exists? "07baca37-5612-4ec6-ae8d-a03f12bd3ff53232"))
  ;(println (dynamo/scan-for-user-id "FREE"))
  ;(println (sgdownloadgen/generate_file "./resources/downloads/sgdownloadjob/input.json" "20241224.113937") )
  (println "End to end done!")
  )