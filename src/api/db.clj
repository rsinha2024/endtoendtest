(ns api.db
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

(defn positions-exist [trade-date created-by]
  (jdbc/with-db-connection [conn db-spec]
                           (let [query "SELECT COUNT(*) FROM agent_lending.bod_positions WHERE trade_date = CAST(? AS DATE) AND created_by = ?"
                                 result (jdbc/query conn [query trade-date created-by])
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


(defn insert-bod-positions-record [bod-record]
  (jdbc/with-db-transaction [t-conn db-spec]
                            (jdbc/execute! t-conn
                                           ["INSERT INTO agent_lending.bod_positions
                    (wlpid, account_no, account_type, symbol, cusip, quantity, trade_date, created_by)
                    VALUES (?, ?, ?::account_type_enum, ?, ?, ?, ?, ?)"
                                            (:wlpid bod-record)
                                            (:accountNo bod-record)
                                            (str (:accountType bod-record))  ;; Cast to string and will be implicitly cast to enum in SQL
                                            (:symbol bod-record)
                                            (:cusip bod-record)
                                            (:quantity bod-record)
                                            (Date/valueOf (.toString (:tradeDate bod-record)))   ;; Format LocalDate to String (or use the appropriate format)
                                            (:createdBy bod-record)])))

;; Creating and inserting the first BodPositionsRecord
(def bod-positions-record-1
  {:wlpid "FREE"
   :accountNo "DWEB000074"
   :accountType "C"                          ;; Enum value as string
   :symbol "BYND"
   :cusip "037833100"
   :quantity (BigDecimal. "10000.00000000")   ;; BigDecimal for quantity
   :tradeDate (LocalDate/parse "2024-09-25")  ;; Parse LocalDate from string
   :createdBy "end2end"})

;; Insert bod-positions-record-1 into the database
;(insert-bod-positions-record bod-positions-record-1)

;; Creating and inserting the second BodPositionsRecord
(def bod-positions-record-2
  {:wlpid       "FREE"
   :accountNo   "DWEB000074"
   :accountType "C"                          ;; Enum value as string
   :symbol "SPY"
   :cusip "594918104"
   :quantity (BigDecimal. "50000.00000000")  ;; BigDecimal for quantity
   :tradeDate (LocalDate/parse "2024-09-25")  ;; Parse LocalDate from string
   :createdBy "end2end"})

;; Creating and inserting the first BodPositionsRecord
(def bod-positions-record-3
  {:wlpid "FREE"
   :accountNo "DWEB000074"
   :accountType "C"                          ;; Enum value as string
   :symbol "MSFT"
   :cusip "594918104"
   :quantity (BigDecimal. "50.00000000")   ;; BigDecimal for quantity
   :tradeDate (LocalDate/parse "2024-09-10")  ;; Parse LocalDate from string
   :createdBy "end2end"})

;; Insert bod-positions-record-1 into the database
;(insert-bod-positions-record bod-positions-record-3)

;; Creating and inserting the second BodPositionsRecord
(def bod-positions-record-4
  {:wlpid "FREE"
   :accountNo "DWEB000074"
   :accountType "C"                          ;; Enum value as string
   :symbol "AAPL"
   :cusip "594918104"
   :quantity (BigDecimal. "500000.00000000")  ;; BigDecimal for quantity
   :tradeDate (LocalDate/parse "2024-09-10")  ;; Parse LocalDate from string
   :createdBy "end2end"})

;; Insert bod-positions-record-2 into the database
;(insert-bod-positions-record bod-positions-record-4)

;; Creating and inserting the third BodPositionsRecord
(def bod-positions-record-5
  {:wlpid "FREE"
   :accountNo "DWEB000074"
   :accountType "C"                          ;; Enum value as string
   :symbol "HDFC"
   :cusip "394918105"
   :quantity (BigDecimal. "500000.00000000")  ;; BigDecimal for quantity
   :tradeDate (LocalDate/parse "2024-09-10")  ;; Parse LocalDate from string
   :createdBy "end2end"})


;; Insert bod-positions-record-3 into the database
;(insert-bod-positions-record bod-positions-record-5)
(defn create-bod-positions [trade_date]
  (insert-bod-positions-record (replace-key-value bod-positions-record-1 :tradeDate trade_date))
  (insert-bod-positions-record (replace-key-value bod-positions-record-2 :tradeDate trade_date))
  (insert-bod-positions-record (replace-key-value bod-positions-record-3 :tradeDate trade_date))
  (insert-bod-positions-record (replace-key-value bod-positions-record-4 :tradeDate trade_date))
  (insert-bod-positions-record (replace-key-value bod-positions-record-5 :tradeDate trade_date))
  )

(defn execute-query-and-compare [query expected-row-count]
  (jdbc/with-db-connection [conn db-spec]
                           (let [results (jdbc/query conn [query])        ;; Execute query
                                 row-count (count results)]                ;; Count the rows
                             (println "Returned rows:" ( :count (first results)))
                             (if (= ( :count (first results)) expected-row-count)         ;; Compare to expected count
                               (println "Test passed: Row count matches expected value.")
                               (println "Test failed: Row count does not match expected value.")))))
