(ns file.generator.trade-continous-map
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [java-time :as jt]
            )
  (:import (java.time LocalDateTime)
           (java.time.format DateTimeFormatter)))

;; CSV header as a vector of column names
(def header ["Date-Time" "Booking Type" "Account Number" "Security Name" "ISIN" "Ticker/ Symbol"
             "Ext Instrument Id" "Trade Date" "Collateral Date" "Settlement Date" "End Date"
             "Units/ Quantity" "Loan Currency" "Price" "Lending Rate (bps)" "Collateral Type"
             "Dividend Rate" "Trade ID" "Link Trade ID" "Ext Borrower Code" "Counterparty Code"
             "Counterparty Name" "Counterparty BIC" "Counterparty Local Code Type" "Counterparty Local Code"
             "Counterparty Account Number" "Settlement Agent Name" "Settlement Agent BIC"
             "Settlement Agent Local Code Type" "Settlement Agent Local Code" "Settlement Agent Account Number"
             "Additional Information" "PSET" "PSET Code"])

;; Function to generate a CSV row from the data map
(defn generate-row
  [{:keys [date-time account-number isin ticker-symbol units price lending-rate collateral-type trade-id]}]
  ;; Return a CSV row with the dynamic data (date-time, account-number, isin, ticker-symbol)
  [date-time "FOP Delivery" account-number "Beyond Meat Inc" isin ticker-symbol "" date-time
   date-time date-time "" units "GBP" price lending-rate collateral-type "100" (str "L2433-V" trade-id) "" "" "MSINT"
   "Morgan Stanley International" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" (str trade-id)
   "" "" "DTCYUS33XXX"])

;; Function to write the data to a CSV file
(defn write-csv
  [filename data]
  (let [file (io/file filename)]
    (io/make-parents file)  ;; Ensure parent directories exist
    (with-open [w (io/writer file)]
      ;; Write header first
      ;  (.write w (str/join "," header))
      ;(.newLine w)

      ;; Write rows from the data
      (doseq [row data]
        (.write w (str/join "," row))
        (.newLine w)))))

;; Function to read JSON data from a file
(defn read-json
  [filename]
  (with-open [r (io/reader filename)]
    (json/read r :key-fn keyword)))  ;; Pass the reader directly

;; Function to generate a formatted date-time string for the filename
(defn generate-file-name
  [business-date]
  (let [formatter (DateTimeFormatter/ofPattern "yyyyMMdd.HHmmss")  ;; Pattern for parsing and formatting
        parsed-date (LocalDateTime/parse business-date formatter)]  ;; Parse the input date
    (.format parsed-date formatter)))  ;; Format back to string

;; Main function to read input data from file and generate CSV output
(defn generate_file
  [file_path date]
  (let [input-data (read-json file_path)
        business-date date ;; Example business date (adjust as necessary)

        ;; Generate the file name based on the business date
        file-name (str "resources/output/DW.TRADE_INSTRUCTION.CONTINUOUS." (generate-file-name business-date) ".csv")

        ;; Generate rows for the CSV based on the input data
        rows (map generate-row input-data)
        data (cons header rows)] ;; Add header to the rows

    ;; Write data to the CSV file in the specific directory
    (write-csv file-name data)))

