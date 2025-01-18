(ns file.generator.monthly-accrual
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [util.properties :as p])
  (:import (java.time LocalDate LocalTime)
           (java.time.format DateTimeFormatter)))





(defn  get-data [billing_month billing_start billing_end]
  [["Billing Month" "Billing Start Period" "Billing End Period" "Lender Name" "Lender Code" "Lender Account Number" "Borrower Name" "Borrower Code" "Loan ID" "Actual Loan ID" "Loan Status" "Loan Type" "Collateral Type" "Withholding Tax Rate (%)" "Margin (%)" "Trade Date" "Collateral Date" "Settlement Date" "End Date" "Instrument Name" "ISIN" "Composite Ticker" "Country Ticker" "BBGID" "Ext Instrument ID" "Instrument Type" "Country of Incorporation" "Country of Listing" "Accrual Start Date" "Accrual End Date" "Days" "Quantity" "Market Price" "Market Ccy" "FX" "Loan Price" "Loan Value" "Loan Ccy" "Investment Rate" "Investment Amount" "Rebate Rate" "Rebate Amount" "Lending Rate" "Day Count Basis" "Fee Split" "Gross Accrual" "Net Accrual" "Agent Fees" "Adjustment Reason"]
   ["September 2024" "2024-09-01" "2024-09-31" "Freetrade Ltd" "FREETRAD" "FTDWACC123" "Morgan Stanley Intrenational" "MSINT" "L2438" "L2438" "Open" "Open-End" "Non-Cash" "100" "105" "2024-09-16" "2024-09-16" "2024-09-16" "" "Gamestop Corp-class A" "US36467W1099" "GME US" "GME US" "BBG000BB5BF6" "" "Stock" "US" "US" "2024-09-16" "2024-09-16" "1" "15000" "20.65" "USD" "0.762079" "15.736931" "236053.97" "GBP" "" "" "0.2800%" "365" "0.8" "1.810825" "0.362165" "1.448660"]
   ["September 2024" "2024-09-01" "2024-09-31" "Freetrade Ltd" "FREETRAD" "FTDWACC123" "Morgan Stanley Intrenational" "MSINT" "L2438" "L2438" "Open" "Open-End" "Non-Cash" "100" "105" "2024-09-17" "2024-09-17" "2024-09-17" "" "Gamestop Corp-class A" "US36467W1099" "GME US" "GME US" "BBG000BB5BF6" "" "Stock" "US" "US" "2024-09-17" "2024-09-17" "1" "15000" "20.08" "USD" "0.756659" "15.193713" "227905.69" "GBP" "" "" "0.2800%" "365" "0.8" "1.748318" "0.349664" "1.398654"]
   ["September 2024" "2024-09-01" "2024-09-31" "Freetrade Ltd" "FREETRAD" "FTDWACC123" "Morgan Stanley Intrenational" "MSINT" "L2438" "L2438" "Open" "Open-End" "Non-Cash" "100" "105" "2024-09-18" "2024-09-18" "2024-09-18" "" "Gamestop Corp-class A" "US36467W1099" "GME US" "GME US" "BBG000BB5BF6" "" "Stock" "US" "US" "2024-09-18" "2024-09-18" "1" "15000" "20.16" "USD" "0.759878" "15.319140" "229787.11" "GBP" "" "" "0.2800%" "365" "0.8" "1.762750" "0.352550" "1.410200"]
   ["September 2024" "2024-09-01" "2024-09-31" "Freetrade Ltd" "FREETRAD" "FTDWACC123" "Morgan Stanley Intrenational" "MSINT" "L2438" "L2438" "Open" "Open-End" "Non-Cash" "100" "105" "2024-09-19" "2024-09-19" "2024-09-19" "" "Gamestop Corp-class A" "US36467W1099" "GME US" "GME US" "BBG000BB5BF6" "" "Stock" "US" "US" "2024-09-19" "2024-09-19" "1" "15000" "19.65" "USD" "0.756888" "14.872849" "223092.74" "GBP" "" "" "0.2800%" "365" "0.8" "1.711396" "0.342279" "1.369117"]
   ["September 2024" "2024-09-01" "2024-09-31" "Freetrade Ltd" "FREETRAD" "FTDWACC123" "Morgan Stanley Intrenational" "MSINT" "L2440" "L2440" "Open" "Open-End" "Non-Cash" "100" "105" "2024-09-16" "2024-09-16" "2024-09-16" "" "Tesla Inc" "US88160R1014" "TSLA US" "TSLA US" "BBG000N9MNX3" "" "Stock" "US" "US" "2024-09-16" "2024-09-16" "1" "789" "230.29" "USD" "0.762079" "175.499173" "138468.85" "GBP" "" "" "0.1100%" "365" "0.8" "0.417303" "0.083461" "0.333843"]
   ["September 2024" "2024-09-01" "2024-09-31" "Freetrade Ltd" "FREETRAD" "FTDWACC123" "Morgan Stanley Intrenational" "MSINT" "L2440" "L2440" "Open" "Open-End" "Non-Cash" "100" "105" "2024-09-17" "2024-09-17" "2024-09-17" "" "Tesla Inc" "US88160R1014" "TSLA US" "TSLA US" "BBG000N9MNX3" "" "Stock" "US" "US" "2024-09-17" "2024-09-17" "1" "789" "226.79" "USD" "0.756659" "171.602695" "135394.53" "GBP" "" "" "0.1100%" "365" "0.8" "0.408038" "0.081608" "0.326431"]
   ["September 2024" "2024-09-01" "2024-09-31" "Freetrade Ltd" "FREETRAD" "FTDWACC123" "Morgan Stanley Intrenational" "MSINT" "L2440" "L2440" "Open" "Open-End" "Non-Cash" "100" "105" "2024-09-18" "2024-09-18" "2024-09-18" "" "Tesla Inc" "US88160R1014" "TSLA US" "TSLA US" "BBG000N9MNX3" "" "Stock" "US" "US" "2024-09-18" "2024-09-18" "1" "789" "227.87" "USD" "0.759878" "173.153400" "136618.03" "GBP" "" "" "0.1100%" "365" "0.8" "0.411726" "0.082345" "0.329380"]
   ["September 2024" "2024-09-01" "2024-09-31" "Freetrade Ltd" "FREETRAD" "FTDWACC123" "Morgan Stanley Intrenational" "MSINT" "L2440" "L2440" "Open" "Open-End" "Non-Cash" "100" "105" "2024-09-19" "2024-09-19" "2024-09-19" "" "Tesla Inc" "US88160R1014" "TSLA US" "TSLA US" "BBG000N9MNX3" "" "Stock" "US" "US" "2024-09-19" "2024-09-19" "1" "789" "227.20" "USD" "0.756888" "171.964954" "135680.35" "GBP" "" "" "0.1100%" "365" "0.8" "0.408900" "0.081780" "0.327120"]])

(defn write-csv [filename data]
  (with-open [writer (io/writer filename)]
    (csv/write-csv writer data)))


(defn generate-csv [file-name billing_month billing_start billing_end]
  (with-open [w (io/writer file-name)]
    (csv/write-csv w (cons headers (get-data billing_month billing_start billing_end) ))))
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
  (str (p/prop "MONTHLY_ACCRUAL_PATH") "/" (p/prop "MONTHLY_ACCRUAL_FILE_FORMAT") "." (convert-date-format business-date) "." (current-time-hhmmss)  ".csv"))


  (defn generate_file [billing_month billing_start billing_end]
  (let [sample-data (get-data billing_month billing_start billing_end)
        file-name (generate-file-name trade_date)]
    (generate-csv file-name billing_month billing_start billing_end)
    (println "csv file generated: " file-name)
    {:file-name file-name
     :data sample-data}))