(ns file.generator.daily-accrual
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [util.properties :as p]
            )
  (:import (org.apache.poi.xssf.usermodel XSSFWorkbook)
           (org.apache.poi.ss.usermodel Row Cell)
           (java.io FileOutputStream)
           (java.time LocalDate)
           (java.time LocalTime)
           (java.time.format DateTimeFormatter)))

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
  (str (p/prop "SGDOWNLOAD_PATH") "/" (p/prop "SGDOWNLOAD_FILE_FORMAT") "." (convert-date-format business-date) "." (current-time-hhmmss)  ".csv"))


(defn generate-sample-data []
  [{:accrual-date "12/16/24" :settled-units 187 :market-price 41 :market-currency "USD" :fx 0.792519 :loan-price 32.493279 :loan-value 6076.24 :loan-currency "GBP" :investment-rate nil :investment-amount nil :rebate-rate 40 :rebate-amount 365 :lending-rate 75 :day-count-basis 6.658897 :fee-split 1.664724 :accrual-gross 4.994172 :accrual-agent "FREETRAD" :accrual-net "Freetrade Ltd" :lender-account-number "FRVV000001" :ext-borrower-code nil :borrower-code "BAML" :borrower-name "Bank of America Merrill Lynch" :loan-id "L29267" :loan-status "Open" :loan-type "Open-End" :collateral-type "Non-Cash" :withholding-tax-rate 100 :margin 105 :trade-date "11/6/24" :collateral-date "11/6/24" :settlement-date "11/6/24" :end-date "Tempus Ai Inc" :isin "US88023B1035" :composite-ticker "TEM US" :country-ticker "TEM US" :bbgid "BBG01MZQYJF9" :ext-instrument-id nil :instrument-type "Equity" :country-of-incorporation "US" :country-of-listing "US"}

   {:accrual-date "12/16/24" :settled-units 90271 :market-price 3.4 :market-currency "USD" :fx 0.792519 :loan-price 2.694565 :loan-value 243241.04 :loan-currency "GBP" :investment-rate nil :investment-amount nil :rebate-rate 6.5 :rebate-amount 365 :lending-rate 75 :day-count-basis 43.316898 :fee-split 10.829224 :accrual-gross 32.487673 :accrual-agent "FREETRAD" :accrual-net "Freetrade Ltd" :lender-account-number "FRVV000001" :ext-borrower-code nil :borrower-code "BAML" :borrower-name "Bank of America Merrill Lynch" :loan-id "L29263" :loan-status "Open" :loan-type "Open-End" :collateral-type "Non-Cash" :withholding-tax-rate 100 :margin 105 :trade-date "11/6/24" :collateral-date "11/6/24" :settlement-date "11/6/24" :end-date "Eos Energy Enterprises Inc" :isin "US29415C1018" :composite-ticker "EOSE US" :country-ticker "EOSE US" :bbgid "BBG00V1KJ6F5" :ext-instrument-id nil :instrument-type "Equity" :country-of-incorporation "US" :country-of-listing "US"}

   {:accrual-date "12/16/24" :settled-units 7470 :market-price 6.48 :market-currency "USD" :fx 0.792519 :loan-price 5.135523 :loan-value 38362.36 :loan-currency "GBP" :investment-rate nil :investment-amount nil :rebate-rate 6 :rebate-amount 365 :lending-rate 75 :day-count-basis 6.306141 :fee-split 1.576535 :accrual-gross 4.729606 :accrual-agent "FREETRAD" :accrual-net "Freetrade Ltd" :lender-account-number "FRVV000001" :ext-borrower-code nil :borrower-code "MSINT" :borrower-name "Morgan Stanley International Equity" :loan-id "L29895" :loan-status "Open" :loan-type "Open-End" :collateral-type "Non-Cash" :withholding-tax-rate 100 :margin 105 :trade-date "11/13/24" :collateral-date "11/13/24" :settlement-date "11/13/24" :end-date "Virgin Galactic Holdings Inc" :isin "US92766K4031" :composite-ticker "SPCE US" :country-ticker "SPCE US" :bbgid "BBG00HTN2CQ3" :ext-instrument-id nil :instrument-type "Equity" :country-of-incorporation "US" :country-of-listing "US"}

   {:accrual-date "12/16/24" :settled-units 38699 :market-price 2.43 :market-currency "USD" :fx 0.792519 :loan-price 1.925821 :loan-value 74527.35 :loan-currency "GBP" :investment-rate nil :investment-amount nil :rebate-rate 2 :rebate-amount 365 :lending-rate 75 :day-count-basis 4.083691 :fee-split 1.020923 :accrual-gross 3.062768 :accrual-agent "FREETRAD" :accrual-net "Freetrade Ltd" :lender-account-number "FRVV000001" :ext-borrower-code nil :borrower-code "BAML" :borrower-name "Bank of America Merrill Lynch" :loan-id "L30004" :loan-status "Open" :loan-type "Open-End" :collateral-type "Non-Cash" :withholding-tax-rate 100 :margin 105 :trade-date "11/14/24" :collateral-date "11/14/24" :settlement-date "11/14/24" :end-date "Plug Power Inc" :isin "US72919P2020" :composite-ticker "PLUG US" :country-ticker "PLUG US" :bbgid "BBG000C1XSP8" :ext-instrument-id nil :instrument-type "Equity" :country-of-incorporation "US" :country-of-listing "US"}

   {:accrual-date "12/16/24" :settled-units 2600 :market-price 6.42 :market-currency "USD" :fx 0.792519 :loan-price 5.087972 :loan-value 13228.73 :loan-currency "GBP" :investment-rate nil :investment-amount nil :rebate-rate 22 :rebate-amount 365 :lending-rate 75 :day-count-basis 7.973479 :fee-split 1.99337 :accrual-gross 5.98011 :accrual-agent "FREETRAD" :accrual-net "Freetrade Ltd" :lender-account-number "FRVV000001" :ext-borrower-code nil :borrower-code "BAML" :borrower-name "Bank of America Merrill Lynch" :loan-id "L30537" :loan-status "Open" :loan-type "Open-End" :collateral-type "Non-Cash" :withholding-tax-rate 100 :margin 105 :trade-date "11/21/24" :collateral-date "11/21/24" :settlement-date "11/21/24" :end-date "Luminar Technologies Inc" :isin "US5504243032" :composite-ticker "LAZR US" :country-ticker "LAZR US" :bbgid "BBG00MS90LK2" :ext-instrument-id nil :instrument-type "Equity" :country-of-incorporation "US" :country-of-listing "US"}

   {:accrual-date "12/16/24" :settled-units 6900 :market-price 3.84 :market-currency "USD" :fx 0.792519 :loan-price 3.043273 :loan-value 20998.58 :loan-currency "GBP" :investment-rate nil :investment-amount nil :rebate-rate 25 :rebate-amount 365 :lending-rate 75 :day-count-basis 14.382591 :fee-split 3.595648 :accrual-gross 10.786944 :accrual-agent "FREETRAD" :accrual-net "Freetrade Ltd" :lender-account-number "FRVV000001" :ext-borrower-code nil :borrower-code "MSINT" :borrower-name "Morgan Stanley International Equity" :loan-id "L29342" :loan-status "Open" :loan-type "Open-End" :collateral-type "Non-Cash" :withholding-tax-rate 100 :margin 105 :trade-date "11/7/24" :collateral-date "11/7/24" :settlement-date "11/7/24" :end-date "Beyond Meat Inc" :isin "US08862E1091" :composite-ticker "BYND US" :country-ticker "BYND US" :bbgid "BBG00G74WZT0" :ext-instrument-id nil :instrument-type "Equity" :country-of-incorporation "US" :country-of-listing "US"}

   {:accrual-date "12/16/24" :settled-units 12293 :market-price 5.32 :market-currency "USD" :fx 0.792519 :loan-price 4.220634 :loan-value 51826.13 :loan-currency "GBP" :investment-rate nil :investment-amount nil :rebate-rate 12 :rebate-amount 365 :lending-rate 75 :day-count-basis 3.655975 :fee-split 0.965831 :accrual-gross 2.865089 :accrual-agent "FREETRAD" :accrual-net "Freetrade Ltd" :lender-account-number "FRVV000001" :ext-borrower-code nil :borrower-code "BAML" :borrower-name "Bank of America Merrill Lynch" :loan-id "L30034" :loan-status "Open" :loan-type "Open-End" :collateral-type "Non-Cash" :withholding-tax-rate 100 :margin 105 :trade-date "11/14/24" :collateral-date "11/14/24" :settlement-date "11/14/24" :end-date "Plug Power Inc" :isin "US72919P2020" :composite-ticker "PLUG US" :country-ticker "PLUG US" :bbgid "BBG000C1XSP8" :ext-instrument-id nil :instrument-type "Equity" :country-of-incorporation "US" :country-of-listing "US"}

   ;; Continue with the rest of the rows similarly...

   ;; Repeat this format for all the rows from your data (this will be the pattern)

   ])

(defn write-to-xlsx [data file-path]
  (let [workbook (XSSFWorkbook.)]
    (let [sheet (.createSheet workbook "Loans")]

      ;; Create header row
      (let [header (first data)
            header-row (.createRow sheet 0)]
        (doseq [col-idx (range (count header))]
          (let [cell (.createCell header-row col-idx)]
            (.setCellValue cell (name (key (nth header col-idx))))))

        ;; Add data rows
        (doseq [i (range (count data))]
          (let [row-data (nth data i)
                row (.createRow sheet (inc i))]
            (doseq [col-idx (range (count row-data))]
              (let [cell (.createCell row col-idx)
                    value (val (nth row-data col-idx))]
                (.setCellValue cell (str value)))))

          ;; Write to file
          (with-open [out (FileOutputStream. file-path)]
            (.write workbook out)))))))

