;https://drivewealth.atlassian.net/browse/SHAR-18
(ns jobs.aneticsuploadjob
  (:require [api.dynamodb :as dynamo]
            [cheshire.core :as cheshire]
            [api.s3 :as s3]
            [util.properties :as p]
            [api.client :as client]
            [clj-http.client :as httpclient]
            ))
(def url (str  ( p/prop "BASE_URL") "/api/v1/anetics/files/upload"))
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
(defn parsed-response [json-str] (cheshire.core/parse-string json-str true))  ;; `true` for keyword keys

(defn setup [trade_date]
  (let [user_id (dynamo/scan-for-user-id "FREE")]
    (if (nil? user_id)
      (println "user id is nil exitting....")
      (do
        (println "In setup" user_id)
        { :user_id user_id}))))

(defn send-request [body]
  (let [response (send-post-request body)]  ;; Call the send-post-request function from the api.client module
    (if (= 200 (:status response))  ;; Check if the request was successful (status 200)
      (do
        (println "Request succeeded!")
        (println "Response body:" (parse-response response))
        ( :jobId (parsed-response (:body response))))  ;; Parse and print the response body
      (println "Request failed with status:" (:status response)))))


(defn trigger-job [user_id trade_date]
  (let [body {:partners [user_id]
              :tradeDate trade_date}]
    (send-request body)
    ))
(defn validate [job_id]
  (println "Validating the job " job_id)
  )
(defn workflow [trade_date]
  (let [{:keys [user_id] } (setup trade_date)
        job_id (trigger-job user_id trade_date)]
        {:job-id job_id}
    ))


