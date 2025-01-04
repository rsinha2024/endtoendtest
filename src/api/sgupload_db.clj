(ns api.sgupload_db
  (:require [clojure.java.jdbc :as jdbc]
            [util.properties :as p]
            [clojure.java.jdbc :as jdbc]
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

;; Function to query the database (mocked here for example)
(defn get-results-from-db []
  (jdbc/with-db-connection [conn db-spec]
                           (let [transactions (jdbc/query conn
                                                          ["SELECT agent_loan_requests_id, trade_id, partner_ibid, quantity, settle_date, status
                                     FROM agent_lending.agent_loan_requests
                                     WHERE (sent_to_agent = true)
                                       AND created_by = ?"
                                                           "end2end"])]
                             (println "Transactions found:" transactions)
                             transactions)))
;; Function to query transactions from the database using `with-db-connection`
(defn get-transactions-from-db []
  (jdbc/with-db-connection [conn db-spec]
                           (let [transactions (jdbc/query conn
                                                          ["SELECT agent_loan_requests_id, trade_id, partner_ibid, quantity, settle_date, status
                                     FROM agent_lending.agent_loan_requests
                                     WHERE (sent_to_agent IS NULL OR sent_to_agent = false)
                                       AND created_by = ?"
                                                            "end2end"])]
                             (println "Transactions found:" transactions)
                             transactions)))

;; Insert function with explicit SQL casting for enums
(defn insert-sample-records [partner-ibid trade-date-str]
  (jdbc/with-db-connection [conn db-spec]
                           (let [trade-date (LocalDate/parse trade-date-str) ;; Convert the string to a LocalDate
                                 trade-date-sql (Date/valueOf trade-date)] ;; Convert LocalDate to java.sql.Date for SQL compatibility
                             (doseq [i (range 1 11)] ;; Generate 10 sample records, you can adjust the range
                               (let [trade-id (str "T" (format "%04d" i))
                                     account-no (str "ACCT" (format "%04d" i))
                                     account-id (str "ACCTID" (format "%04d" i))
                                     quantity (* 1000 (rand 10)) ;; Random quantity between 0 and 10,000
                                     settle-date trade-date-sql
                                     trade-type (if (even? i) "LOAN" "RECALL") ;; Alternating trade types for diversity
                                     account-type (if (even? i) "C" "M") ;; Alternating account types (C and M)
                                     loan-rate (rand 0.05)
                                     status "SETTLED"] ;; Random loan rate between 0 and 5%

                                 ;; Build raw SQL with explicit type casting for account_type
                                 (jdbc/execute! conn
                                                ["INSERT INTO agent_lending.agent_loan_requests
                           (partner_ibid, account_no, account_id, account_type, trade_id, trade_type,
                            trade_date, settle_date, quantity, settled_quantity, sent_to_agent,
                            sent_to_intc, created_at, updated_at, created_by, updated_by,
                            symbol, isin, pset, settle_agent_code, comments, inbound_file, outbound_file,status)
                           VALUES (?, ?, ?, ?::account_type_enum, ?, ?::agent_lending_trade_type_enum, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?, ?::agent_lending_status_enum)"
                                                 partner-ibid account-no account-id account-type trade-id trade-type
                                                 trade-date-sql settle-date quantity quantity false false
                                                 (java.time.LocalDateTime/now) (java.time.LocalDateTime/now)
                                                 "end2end" "end2end" (str "SYM" (format "%04d" i))
                                                 (str "ISIN" (format "%04d" i)) (str "PSET" (format "%04d" i))
                                                 (str "AGENT" (format "%04d" i)) (str "Test comment " i)
                                                 "sample_inbound_file.txt" "sample_outbound_file.txt" status]))))))

(defn delete-sample-records []
  (jdbc/with-db-connection [conn db-spec]
                           (jdbc/execute! conn
                                          ["DELETE FROM agent_lending.agent_loan_requests WHERE created_by = ?" "end2end"])))