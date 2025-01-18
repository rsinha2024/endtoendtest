(ns file.generator.daily-accrual
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [util.properties :as p])
  (:import (java.time LocalDate LocalTime)
           (java.time.format DateTimeFormatter)))





(def headers ["Accrual Date", "Settled Units", "Market Price", "Market Currency", "FX", "Loan Price", "Loan Value", "Loan Currency",
              "Investment Rate", "Investment Amount", "Rebate Rate", "Rebate Amount", "Lending Rate", "Day Count Basis",
              "Fee Split", "Accrual (Gross)", "Accrual (Agent)", "Accrual (Net)", "Lender Code", "Lender Name",
              "Lender Account Number", "Ext Borrower Code", "Borrower Code", "Borrower Name", "Loan ID", "Loan Status",
              "Loan Type", "Collateral Type", "Withholding Tax Rate", "Margin", "Trade Date", "Collateral Date",
              "Settlement Date", "End Date", "Instrument Name", "ISIN", "Composite Ticker", "Country Ticker", "BBGID",
              "Ext Instrument Id", "Instrument Type", "Country of Incorporation", "Country of Listing"])

(defn get-data [accrual_date trade_date]
  [[accrual_date 15000 20.650000 "USD" 0.762079 15.736931 236053.97 "GBP" "" "" "" "" "0.280000%" 365 "80.00%" 1.810825 0.362165 1.448660
    "FREETDSB" "Freetrade Limited Sandbox" "FTDWACC123" "" "MSINT" "Morgan Stanley International" "L2438" "Open" "Open-End" "Non-Cash"
    100.000 105 trade_date trade_date trade_date "" "Gamestop Corp-class A" "US36467W1099" "GME US" "GME US" "BBG000BB5BF6" ""
    "Equity" "US" "US"]
   [accrual_date 15000 20.080000 "USD" 0.756659 15193.713 227905.69 "GGB" "" "" "" "" "0.280000%" 365 "80.00%" 1.748318 0.349664 1.398654
    "FREETDSB" "Freetrade Limited Sandbox" "FTDWACC123" "" "MSINT" "Morgan Stanley International" "L2438" "Open" "Open-End" "Non-Cash"
    100.000 105 trade_date trade_date trade_date "" "Gamestop Corp-class A" "US36467W1099" "GME US" "GME US" "BBG000BB5BF6" ""
    "Equity" "US" "US"]])

(defn generate-csv [file-name trade_date]
  (with-open [w (io/writer file-name)]
    (csv/write-csv w (cons headers (get-data trade_date trade_date) ))))
(defn remove-resources-prefix [path]
  (str/replace path #"^resources/" ""))

(defn current-time-hhmmss []
  (let [formatter (DateTimeFormatter/ofPattern "HHmmss")  ;; Define the format
        current-time (LocalTime/now)]  ;; Get the current time
    (.format current-time formatter)))  ;; Format the current time
;;
(defn convert-date-format [input-date]
  (let [input-formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd")    ;; Input format
        output-formatter (DateTimeFormatter/ofPattern "yyyyMMdd")      ;; Output format
        parsed-date (LocalDate/parse input-date input-formatter)]       ;; Parse the input date
    (.format parsed-date output-formatter)))                           ;; Format to output format

(defn extract-filename [file-path]
  (let [file-name (last (str/split file-path #"/"))]  ;; Split by "/" and take the last part
    file-name))
;; Function to generate a formatted date string for the filename eg DW.TRADE_INSTRUCTION.CONTINUOUS.20241223.152551.csv
(defn generate-file-name
  [business-date]
  (str (p/prop "DAILY_ACCRUAL_PATH") "/" (p/prop "DAILY_ACCRUAL_FILE_FORMAT") "." (convert-date-format business-date) "." (current-time-hhmmss)  ".csv"))


  (defn generate_file [trade_date]
  (let [sample-data (get-data trade_date trade_date)
        file-name (generate-file-name trade_date)]
    (generate-csv file-name trade_date)
    (println "csv file generated: " file-name)
    {:file-name file-name
     :data sample-data}))