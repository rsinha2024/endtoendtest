(ns api.db
  (:require [clojure.java.jdbc :as jdbc]))


(def db-spec
  {:dbtype "postgres"
   :host "dwdev-aurora-babelfish-instance-1.csefnakly5ig.us-east-1.rds.amazonaws.com"
   :port 5432
   :dbname "post_trade"
   :user "cost_basis_user"
   :password "@sDfgKJ7g^%g2ug"})

(defn query-db []
  (jdbc/with-db-connection [conn db-spec]
                           (let [results (jdbc/query conn ["SELECT agent_lending_job_tracking_id, batch_job_id, trade_date, process_partner_file, send_file_to_ops_for_anetics, anetics_file_id, process_anetics_file, send_status_file_to_partner, created_at, updated_at, delete_flag\nFROM agent_lending.agent_lending_job_tracking"])]
                             (doseq [row results]
                               (println row))))

  )