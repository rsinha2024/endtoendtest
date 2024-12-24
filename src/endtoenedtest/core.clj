(ns endtoenedtest.core
  (:require [api.client :as api]   ;; Import the API client module
            [cheshire.core :as cheshire]
            [api.db :as db]
            [api.s3 :as s3]
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
  "I don't do a whole lot ... yet."
  [& args]
  (println "End to end started!")
  ; (process-trade)
  ;(db/query-db)
  (s3/upload-test-file)
  (println "End to end done!")
  )
