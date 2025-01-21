(ns jobs.loanet_import_file
  (:require [api.dynamodb :as dynamo]
            [api.db :as db]
            [cheshire.core :as cheshire]
            [file.generator.daily-accrual :as filegen]
            [api.s3 :as s3]
            [util.properties :as p]
            [clj-http.client :as httpclient]
            [api.client :as client]
            [api.loanet_db :as loanetdb]
            ))
(def url (str  ( p/prop "BASE_URL") "/api/v1/loanet/LoanetDaily"))
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


(defn setup [trade_date]
  )


(defn trigger-job [trade_date]
  (let [body {:date trade_date}]
     (process-trade body)
    ))

;; Function to execute SQL query and compare number of rows returned to 7

(defn validate [job_id trade_date]
  (println "Validating...for job_id=" job_id)
  (Thread/sleep 10000)

  (client/poll-job-status job_id 20000 1000)
  (when-not (loanetdb/loanet-records-exist trade_date)
    (println "Loanett table has no positions for " trade_date ))
  )


