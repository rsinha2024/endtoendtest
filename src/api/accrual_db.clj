(ns api.accrual_db
  (:require [clojure.java.jdbc :as jdbc]
            [util.properties :as p]
            )
  (:import (java.time LocalDate)
           (java.sql Date)))


(def db-spec
  {:dbtype (p/prop "DB_TYPE")
   :host (p/prop "DB_HOST")
   :port (p/prop "DB_PORT")
   :dbname (p/prop "DB_NAME")
   :user (p/prop "DB_USER")
   :password (p/prop "DB_PASSWORD")})

(defn query-db []
  (jdbc/with-db-connection [conn db-spec]
                           (let [results (jdbc/query conn ["SELECT agent_lending_job_tracking_id, batch_job_id, trade_date, process_partner_file, send_file_to_ops_for_anetics, anetics_file_id, process_anetics_file, send_status_file_to_partner, created_at, updated_at, delete_flag\nFROM agent_lending.agent_lending_job_tracking"])]
                             (doseq [row results]
                               (println row))))

  )

(defn insert-config [key value]
  (jdbc/with-db-connection [conn db-spec]
                           (jdbc/execute! conn
                                          ["INSERT INTO agent_lending.config (name, config_value, created_at)
                     VALUES (?, ?, CURRENT_TIMESTAMP)
                     ON CONFLICT (name) DO NOTHING"
                                           key value])))

(defn replace-key-value
  [m key new-value]
  (assoc m key new-value))

(defn accrual-positions-exist [trade-date]
  (jdbc/with-db-connection [conn db-spec]
                           (let [query "SELECT COUNT(*) FROM agent_lending.daily_accrual WHERE trade_date = CAST(? AS DATE)"
                                 result (jdbc/query conn [query trade-date])
                                 ]
                               (>  (:count (first result))  1))
                                         ))

;; Function to check if a config key exists
(defn key-exists? [key]
  (jdbc/with-db-connection [conn db-spec]
                           (let [result (jdbc/query conn
                                                    ["SELECT 1 FROM agent_lending.config WHERE name = ? LIMIT 1" key])]
                             (not (empty? result)))))  ;; If result is not empty, the key exists
;; Helper function to insert a BodPositionsRecord


