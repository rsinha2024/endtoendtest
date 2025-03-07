(ns jobs.monthly_accrual
  (:require [api.dynamodb :as dynamo]
            [api.db :as db]
            [cheshire.core :as cheshire]
            [file.generator.monthly-accrual :as filegen]
            [api.s3 :as s3]
            [util.properties :as p]
            [clj-http.client :as httpclient]
            [api.client :as client]
            [api.monthly_accrual_db :as accrualdb]
            ))
(def url (str  ( p/prop "BASE_URL") "/api/v1/monthly/accrual"))
(def headers {"accept"       "application/json"
              "Content-Type" "application/json"})

;; Function to send POST request with JSON payload
(defn send-post-request [body]
  (let [response (httpclient/post url
                                  {:headers headers
                                   :body    (cheshire/generate-string body)})] ;; Convert body to JSON string
    response))

;; Function to parse the response (if needed)
(defn parse-response [response]
  (cheshire/parse-string (:body response) true))
;; Parse the JSON string into a Clojure map
(defn parsed-response [json-str] (cheshire.core/parse-string json-str true))  ;; `true` for keyword keys


;; Function to process the request and handle the response
(defn process-trade [body]
  (let [response (send-post-request body)]  ;; Call the send-post-request function from the api.client module
    (if (= 200 (:status response))  ;; Check if the request was successful (status 200)
      (do
        (println "Request succeeded!")
        (println "Response body:" (parse-response response))
        ( :jobId (parsed-response (:body response))))  ;; Parse and print the response body
      (println "Request failed with status:" (:status response)))))

(defn generate-json [base]
  (let [data {:outbound (str base "/outbound/")
              :inbound  (str base "/inbound/")}]
    (cheshire/generate-string data)))  ;; Convert the map to JSON string


(defn setup [trade_date billing_month billing_start billing_end]
  (let [user_id (dynamo/scan-for-user-id "FREE")]
    (if (nil? user_id)
      (println "user id is nil exitting....")
      (do
        (println "In setup" user_id)
        (when-not (db/key-exists? user_id)
          (println "Inserting into config" user_id)
          (db/insert-config user_id (generate-json "FREE"))
        )
        (when (accrualdb/accrual-positions-exist trade_date)
          (println "Daily Accrual table has positions for " trade_date "Deleting!")
          (accrualdb/delete-accrual-records trade_date)
          (println "Deleted!!!"))
        (let [s3map (filegen/generate_file trade_date billing_month billing_start billing_end)
              s3file (:file-name s3map)
              ]
          (println "Generated file" s3file)
          ;SGDOWNLOAD_FILE_FORMAT
          (s3/upload-file (p/prop "BUCKET_NAME") (filegen/remove-resources-prefix  s3file)    (str "freetrade/inbound/" (filegen/extract-filename s3file)))
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

;; Function to execute SQL query and compare number of rows returned to 7

(defn validate [job_id trade_date]
  (println "Validating...for job_id=" job_id)
  (Thread/sleep 10000)

  (client/poll-job-status job_id 20000 1000)
  (when-not (accrualdb/accrual-positions-exist trade_date)
    (println "monthly Accrual table has no positions for " trade_date ))
  )


