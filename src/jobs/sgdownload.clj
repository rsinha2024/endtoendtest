(ns jobs.sgdownload
  (:require [api.dynamodb :as dynamo]
            [api.db :as db]
            [cheshire.core :as cheshire]))
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
        )))))


;bo.partnerProfiles
; setup data
; ; get userid for wlpid freetrade 07baca37-5612-4ec6-ae8d-a03f12bd3ff5 07baca37-5612-4ec6-ae8d-a03f12bd3ff5
;; config insert userid into config
; execute job
; verify run
