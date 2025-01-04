(ns api.client
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]
            [util.properties :as p]))

;; Define the API URL and headers
;(def url "https://agent-lending-service-posttrade-drivewealth-com.apps.rosadev0.r01r.p1.openshiftapps.com/api/v1/inbound/files/process")
(def url (str  ( p/prop "BASE_URL") "/api/v1/inbound/files/process"))
(def headers {"accept"       "application/json"
              "Content-Type" "application/json"})

;; Function to send POST request with JSON payload
(defn send-post-request [body]
  (let [response (client/post url
                              {:headers headers
                               :body    (cheshire/generate-string body)})] ;; Convert body to JSON string
    response))

;; Function to parse the response (if needed)
(defn parse-response [response]
  (cheshire/parse-string (:body response) true))            ;; Convert JSON to Clojure map with keywords

(defn get-job-status [job-id]
  (let [url (str (p/prop "BASE_URL") "/api/v1/status/" job-id)
        headers {"accept" "application/json"}]
    (try
      (let [response (client/get url {:headers headers :as :json})
            resp (:body response)
            ]
         (println "response=" resp "jobid=" (:jobId resp) "completed=" (:status resp))
         (:status resp)
        )  ;; true to parse into Clojure maps
      (catch Exception e
        (println "Error: " (.getMessage e))
        )))
  )
;; Function to poll the job status
(defn poll-job-status [job-id duration interval]
  (let [end-time (+ (System/currentTimeMillis) duration)]
    (loop []
      (let [status (get-job-status job-id)]   ;; Fetch job status
        (cond
          ;; Stop polling if the job is completed
          (= status "COMPLETED")
          (do
            (println "Job completed successfully.")
            (println "Final Status: " status)
            (println "Polling completed."))

          ;; Stop polling if the job failed
          (= status "FAILED")
          (do
            (println "Job failed.")
            (println "Final Status: " status)
            (println "Polling completed."))

          ;; Continue polling if the job is in progress
          (= status "INPROGRESS")
          (do
            (println "Current Job Status: " status)
            (Thread/sleep interval)    ;; Sleep for the specified interval (in ms)
            (recur))                   ;; Recur to continue polling

          (= status "STARTED")
          (do
            (println "Current Job Status: " status)
            (Thread/sleep interval)    ;; Sleep for the specified interval (in ms)
            (recur))                   ;; Recur to continue polling

          (= status "STARTING")
          (do
            (println "Current Job Status: " status)
            (Thread/sleep interval)    ;; Sleep for the specified interval (in ms)
            (recur))

          ;; Stop polling if we get an unknown status
          :else
          (do
            (println "Unknown status: " status)
            (println "Polling completed.")))))))  ;; Exit polling if the status is unknown
