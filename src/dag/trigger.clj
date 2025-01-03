(ns dag.trigger
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]
            ))

(defn get-dag-run [dag-id]
  (let [url (str "https://6e3634e6-97dc-412c-a2ef-2c80e96e6495-vpce.c67.us-east-1.airflow.amazonaws.com/api/v1/dags/"
                 dag-id "/dagRuns")  ;; Replace {dag_id} with the actual dag-id
        headers {"accept" "application/json"}]
    (try
      (let [response (client/get url {:headers headers :as :json})  ;; Send GET request
            body (:body response)]  ;; Get the parsed body (JSON response)
        (println "Response Body: " body)  ;; Print the body for debugging
        body)  ;; Return the parsed JSON body
      (catch Exception e
        (println e "Error fetching DAG run information for dag-id" dag-id)
        nil))))  ;; Return nil in case of error

;; Function to extract and print details from the DAG run response
(defn extract-dag-run-details [dag-id]
  (let [response-body (get-dag-run dag-id)]  ;; Get the response body
    (when response-body
      (doseq [dag-run response-body]  ;; Iterate through the list of DAG runs (if multiple)
        (let [dag-run-id (:dag_run_id dag-run)
              execution-date (:execution_date dag-run)
              logical-date (:logical_date dag-run)
              note (:note dag-run)]
          (println "DAG Run ID: " dag-run-id)
          (println "Execution Date: " execution-date)
          (println "Logical Date: " logical-date)
          (println "Note: " note))))))
