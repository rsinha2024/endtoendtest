(ns api.client
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]))

;; Define the API URL and headers
(def url "https://agent-lending-service-posttrade-drivewealth-com.apps.rosadev0.r01r.p1.openshiftapps.com/api/v1/inbound/files/process")

(def headers {"accept" "application/json"
              "Content-Type" "application/json"})

;; Function to send POST request with JSON payload
(defn send-post-request [body]
  (let [response (client/post url
                              {:headers headers
                               :body (cheshire/generate-string body)})]  ;; Convert body to JSON string
    response))

;; Function to parse the response (if needed)
(defn parse-response [response]
  (cheshire/parse-string (:body response) true))  ;; Convert JSON to Clojure map with keywords

