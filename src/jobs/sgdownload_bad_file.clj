(ns jobs.sgdownload_bad_file
  (:require [api.dynamodb :as dynamo]
            [api.db :as db]
            [cheshire.core :as cheshire]
            [file.generator.sgdownload_bad_file :as sgdownloadgen]
            [api.s3 :as s3]
            [util.properties :as p]
            [api.client :as client]

            ))

;; Parse the JSON string into a Clojure map
(defn parsed-response [json-str] (cheshire.core/parse-string json-str true))  ;; `true` for keyword keys


;; Function to process the request and handle the response
(defn process-trade [body]
  (let [response (client/send-post-request body)]  ;; Call the send-post-request function from the api.client module
    (if (= 200 (:status response))  ;; Check if the request was successful (status 200)
      (do
        (println "Request succeeded!")
        (println "Response body:" (client/parse-response response))
        ( :jobId (parsed-response (:body response))))  ;; Parse and print the response body
      (println "Request failed with status:" (:status response)))))

(defn generate-json [base]
  (let [data {:outbound (str base "/outbound/")
              :inbound  (str base "/inbound/")}]
    (cheshire/generate-string data)))  ;; Convert the map to JSON string


(defn setup [trade_date]
  (let [user_id (dynamo/scan-for-user-id "FREE")]
    (if (nil? user_id)
      (println "user id is nil exitting....")
      (do
        (println "In setup" user_id)
        (when-not (db/key-exists? user_id)
          (println "Inserting into config" user_id)
          (db/insert-config user_id (generate-json "FREE"))
        )
        (when-not (db/positions-exist trade_date "end2end")
          (println "Inserting into bod positions")
          (db/create-bod-positions trade_date)
          )
        (let [s3map (sgdownloadgen/generate_file (p/prop "SGDOWNLOAD_INPUT_JSON") trade_date )
              s3file (:file-name s3map)
              ]
          (println "Generated file" s3file)
          ;SGDOWNLOAD_FILE_FORMAT
          (s3/upload-file (p/prop "BUCKET_NAME") (sgdownloadgen/remove-resources-prefix  s3file)    (str "freetrade/inbound/" (sgdownloadgen/extract-filename s3file)))
          (println "Uploaded file!!!")
          { :user_id user_id
           :s3file (:file-name s3map)
           :data (:data s3map)
           })


      ))))


;bo.partnerProfiles
; setup data
; ; get userid for wlpid freetrade 07baca37-5612-4ec6-ae8d-a03f12bd3ff5 07baca37-5612-4ec6-ae8d-a03f12bd3ff5
;; config insert userid into config
; execute job
; verify run
(defn trigger-job [user_id trade_date]
  (let [body {:partners [user_id]
              :tradeDate trade_date}]
     (process-trade body)
    ))
(defn extract-trade-ids [data]
  "Extract trade ids from a sequence of vectors at position 18."
  (map #(nth % 17) data))  ;; nth gets the value at index 18

(defn generate-sql-query [data]
  "Generate an SQL query with trade_id values from a sequence of vectors."
  (let [trade-ids (extract-trade-ids data)
        formatted-ids (clojure.string/join "," (map #(str "'" % "'") trade-ids))]   ;; Format IDs as a comma-separated string
    (str "SELECT count(*) FROM agent_lending.agent_loan_requests WHERE trade_id IN (" formatted-ids ")")))

;; Function to execute SQL query and compare number of rows returned to 7

(defn validate [data job_id]
  (println "Validating..." data "for job_id=" job_id)
  (Thread/sleep 10000)
  (let [sql-query (generate-sql-query data)]
       (db/execute-query-and-compare sql-query 7))
  (client/poll-job-status job_id 20000 1000)
  )
(defn workflow [trade_date]
  (let [{:keys [user_id s3file data] } (setup trade_date)
        job_id (trigger-job user_id trade_date)]
    (validate data job_id)))


