(ns jobs.sgdownload
  (:require [api.dynamodb :as dynamo]
            [api.db :as db]
            [cheshire.core :as cheshire]
            [file.generator.trade-continous-map :as sgdownloadgen]
            [api.s3 :as s3]
            [util.properties :as p]
            [api.client :as client]
            ))


;; Function to process the request and handle the response
(defn process-trade [body]
  (let [response (client/send-post-request body)]  ;; Call the send-post-request function from the api.client module
    (if (= 200 (:status response))  ;; Check if the request was successful (status 200)
      (do
        (println "Request succeeded!")
        (println "Response body:" (client/parse-response response)))  ;; Parse and print the response body
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
        (let [s3file (sgdownloadgen/generate_file (p/prop "SGDOWNLOAD_INPUT_JSON") trade_date )
              ]
          (println "Generated file" s3file)
          ;SGDOWNLOAD_FILE_FORMAT
          (s3/upload-file (p/prop "BUCKET_NAME") (sgdownloadgen/remove-resources-prefix  s3file)    (str "freetrade/inbound/" (sgdownloadgen/extract-filename s3file)))
          (println "Uploaded file!!!")
          { :user_id user_id
           :s3file s3file
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
(defn validate []
  )
(defn workflow [trade_date]
  (let [{:keys [user_id s3file] } (setup trade_date)]
    (trigger-job user_id trade_date)
    (validate)))


