(ns util.general
  (:require [clojure.string :as str]
            [clojure.test :refer :all]))

(def test "accrual_date,settled_units,market_price,market_currency,fx,loan_price,loan_value,loan_currency,investment_rate,investment_amount,rebate_rate,rebate_amount,lending_rate,day_count_basis,fee_split,accrual_gross,accrual_agent,accrual_net,lender_code,lender_name,lender_account_number,ext_borrower_code,borrower_code,borrower_name,loan_id,loan_status,loan_type,collateral_type,withholding_tax_rate,margin,trade_date,collateral_date,settlement_date,end_date,instrument_name,isin,composite_ticker,country_ticker,bbgid,ext_instrument_id,instrument_type,country_of_incorporation,country_of_listing")
(def row1 "11/20/24,0,25.26,USD,0.788519,19.91799,0,GBP,,,90,365,75,0,0,0,FREETRAD,Freetrade Ltd,FRVV000001,,MSINT,Morgan Stanley International Equity,L30178,Closed,Open-End,Non-Cash,100,105,11/18/24,11/18/24,11/18/24,11/20/24,Nano Nuclear Energy Inc,US63010H1086,NNE US,NNE US,BBG01F7QNWC1,,Equity,US,US")
(defn count-commas [input]
  (dec (count (str/split input #","))))

(deftest test-count-commas
  (is (= 42 (count-commas test)))  ;; Replace 42 with the expected count of commas
  (is (= 42 (count-commas row1)))  ;; Replace 42 with the expected count of commas
  (is (= 0 (count-commas "no commas here")))
  (is (= 1 (count-commas "one,comma")))
  (is (= 2 (count-commas ",leading,comma")))
  (is (= 2 (count-commas "trailing,comma,"))))

;; Run the tests
(run-tests)
