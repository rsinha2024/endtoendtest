(ns file.generator.daily-accrual
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [util.properties :as p])
  (:import (java.time LocalDate LocalTime)
           (java.time.format DateTimeFormatter)))




(def data
  ; accrual_date  settled_units  market_price  market_currency   fx          loan_price  loan_value  loan_currency  investment_rate     investment_amount  rebate_rate   rebate_amount  "lending_rate" "day_count_basis" "fee_split" "accrual_gross" "accrual_agent" "accrual_net"          "lender_code"      "lender_name"      "lender_account_number"  "ext_borrower_code" "borrower_code" "borrower_name"                          "loan_id"      "loan_status" "loan_type"   "collateral_type" "withholding_tax_rate" "margin" "trade_date" "collateral_date" "settlement_date" "end_date" "instrument_name"          "isin"         "composite_ticker" "country_ticker" "bbgid"       "ext_instrument_id" "instrument_type" "country_of_incorporation" "country_of_listing"])
  [["11/20/24"     10            25.26          "USD"            0.788519    19.91799     0          "GBP"          "0.45"               "40000"           90            365            75                0                 0           0                 ""           0.0                   "FREETRAD"       "Freetrade Ltd"      "FRVV000001"              ""                   "MSINT"       "Morgan Stanley International Equity"    "L30178"      "Closed"      "Open-End"    "Non-Cash"         100                     105     "11/18/24"   "11/18/24"        "11/18/24"        "11/20/24" "Nano Nuclear Energy Inc" "US63010H1086" "NNE US"            "NNE US"        "BBG01F7QNWC1" ""                  "Equity"          "US"                        "US"]
   ["11/20/24"    1518           27.02          "USD"            0.788519    21.305783   32342.18    "GBP"           ""                   ""               4.5           365            75               3.987392         0.996848     2.990544                                              "FREETRAD"       "Freetrade Ltd" "FRVV000001" "" "BAML" "Bank of America Merrill Lynch" "L30440" "Open" "Open-End" "Non-Cash" 100 105 "11/20/24" "11/20/24" "11/20/24" "" "Nuscale Power Corp" "US67079K1007" "SMR US" "SMR US" "BBG00YG48NM6" "" "Equity" "KY" "US"]
   ["11/21/24"    2000           15.45          "USD"            0.789001    18.337      10000       "EUR"           4.8                  15000            1.25          0.1 3.5 360 50 4000                                                                                                 "FREETRAD"       "Freetrade Ltd" "FRVV000002" "" "JPMS" "JPMorgan Securities" "L30441" "Active" "Open-End" "Cash" 50 100 "11/19/24" "11/19/24" "11/20/24" "11/21/24" "Horizon Energy" "US5146891016" "HE US" "HE US" "BBG01F7QNWC2" "" "Debt" "US" "US"]
   ["11/21/24" 300 33.12 "GBP" 1.30 29.987 50000 "USD" 5.5 30000 0.9 0.5 4.2 360 25 2000                                                                                                                                                                                                     "FREETRAD"       "Freetrade Ltd" "FRVV000003" "" "GS" "Goldman Sachs" "L30442" "Closed" "Closed-End" "Cash" 40 75 "11/20/24" "11/20/24" "11/21/24" "11/21/24" "AstraZeneca" "GB0009895292" "AZN LN" "AZN LN" "BBG01F7QNWC3" "" "Bond" "GB" "GB"]
   ["11/22/24" 5000 55.33 "USD" 0.799334 22.312 25000 "AUD" 6.0 25000 1.5 0.8 3.2 365 80 10000                                                                                                                                                                                               "FREETRAD"       "Freetrade Ltd" "FRVV000004" "" "CITI" "Citigroup" "L30443" "Active" "Open-End" "Non-Cash" 60 90 "11/21/24" "11/21/24" "11/22/24" "11/23/24" "Tesla" "US88160R1014" "TSLA US" "TSLA US" "BBG01F7QNWC4" "" "Equity" "US" "US"]
   ["11/23/24" 122 12.45 "CAD" 0.740000 15.346 2000 "USD" 5.3 7000 0.75 0.4 2.9 360 40 1500                                                                                                                                                                                                  "FREETRAD" "Freetrade Ltd" "FRVV000005" "" "MS" "Morgan Stanley" "L30444" "Closed" "Closed-End" "Cash" 35 50 "11/22/24" "11/22/24" "11/23/24" "11/24/24" "Spotify" "US8491041037" "SPOT US" "SPOT US" "BBG01F7QNWC5" "" "Debt" "US" "US"]])

(def headers ["Accrual Date", "Settled Units", "Market Price", "Market Currency", "FX", "Loan Price", "Loan Value", "Loan Currency",
              "Investment Rate", "Investment Amount", "Rebate Rate", "Rebate Amount", "Lending Rate", "Day Count Basis",
              "Fee Split", "Accrual (Gross)", "Accrual (Agent)", "Accrual (Net)", "Lender Code", "Lender Name",
              "Lender Account Number", "Ext Borrower Code", "Borrower Code", "Borrower Name", "Loan ID", "Loan Status",
              "Loan Type", "Collateral Type", "Withholding Tax Rate", "Margin", "Trade Date", "Collateral Date",
              "Settlement Date", "End Date", "Instrument Name", "ISIN", "Composite Ticker", "Country Ticker", "BBGID",
              "Ext Instrument Id", "Instrument Type", "Country of Incorporation", "Country of Listing"])

(def data1
  [["2024-09-16" 15000 20.650000 "USD" 0.762079 15.736931 236053.97 "GBP" "" "" "" "" "0.280000%" 365 "80.00%" 1.810825 0.362165 1.448660
    "FREETDSB" "Freetrade Limited Sandbox" "FTDWACC123" "" "MSINT" "Morgan Stanley International" "L2438" "Open" "Open-End" "Non-Cash"
    100.000 105 "2024-09-16" "2024-09-16" "2024-09-16" "" "Gamestop Corp-class A" "US36467W1099" "GME US" "GME US" "BBG000BB5BF6" ""
    "Equity" "US" "US"]
   ["2024-09-17" 15000 20.080000 "USD" 0.756659 15193.713 227905.69 "GGB" "" "" "" "" "0.280000%" 365 "80.00%" 1.748318 0.349664 1.398654
    "FREETDSB" "Freetrade Limited Sandbox" "FTDWACC123" "" "MSINT" "Morgan Stanley International" "L2438" "Open" "Open-End" "Non-Cash"
    100.000 105 "2024-09-16" "2024-09-16" "2024-09-16" "" "Gamestop Corp-class A" "US36467W1099" "GME US" "GME US" "BBG000BB5BF6" ""
    "Equity" "US" "US"]])

(defn generate-csv [file-name trade_date]
  (with-open [w (io/writer file-name)]
    (csv/write-csv w (cons headers data1))))
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
  (let [sample-data data1
        file-name (generate-file-name trade_date)]
    (generate-csv file-name trade_date)
    (println "csv file generated: " file-name)
    {:file-name file-name
     :data sample-data}))