(ns api.dynamodb
       (:require [amazonica.aws.dynamodbv2 :as dynamodb]))


  (defn scan-for-user-id [wlpid]
        (let [table-name "bo.partnerProfiles"
              request {:table-name table-name
                       :filter-expression "wlpID = :wlpid"
                       :expression-attribute-values {":wlpid" {:s wlpid}}}]
          (println "Scan request being sent: " request)  ;; Debugging line
          (try
            (let [response (dynamodb/scan request)]  ;; Using scan to query based on wlpID
              (println "Scan response: " response)  ;; Log full response for debugging
              (if (seq (:items response))  ;; Check if any items are returned
                (let [user-id (:userID (first (:items response)))]  ;; Extract userID from the first item
                  (println "UserID: " user-id)
                  user-id
                  )
                (println "No items found for wlpID:" wlpid)))  ;; Item not found
            (catch Exception e
              (println "Error scanning for user for wlpID" wlpid ": " (.getMessage e))  ;; Log exception message
              (println "Full Exception: " (str e))  ;; Log full exception (stack trace)
              nil))))  ;; Return nil in case of an error



  ;; Example usage
