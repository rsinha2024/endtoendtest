(ns file.generator.trade-continous)
  (:require [clojure.java.io :as io])

(def data
  [["Date-Time" "Booking Type" "Account Number" "Security Name" "ISIN" "Ticker/ Symbol" "Ext Instrument Id" "Trade Date" "Collateral Date" "Settlement Date" "End Date" "Units/ Quantity" "Loan Currency" "Price" "Lending Rate (bps)" "Collateral Type" "Dividend Rate" "Trade ID" "Link Trade ID" "Ext Borrower Code" "Counterparty Code" "Counterparty Name" "Counterparty BIC" "Counterparty Local Code Type" "Counterparty Local Code" "Counterparty Account Number" "Settlement Agent Name" "Settlement Agent BIC" "Settlement Agent Local Code Type" "Settlement Agent Local Code" "Settlement Agent Account Number" "Additional Information" "PSET" "PSET Code"]
   ["2024-12-16" "FOP Delivery" "DWEB000074" "Beyond Meat Inc" "US08862E1091" "BYND US" "" "2024-12-16" "2024-12-16" "2024-12-16" "" "5000" "GBP" "4.52" "8428" "Non-Cash" "100" "L2433-V1" "" "" "MSINT" "Morgan Stanley Intrenational" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" "5198" "" "" "DTCYUS33XXX"]
   ["2024-12-16" "FOP Delivery" "DWEB000074" "Beyond Meat Inc" "US08862E1091" "BYND US" "" "2024-12-16" "2024-12-16" "2024-12-16" "" "4500" "GBP" "4.52" "8428" "Non-Cash" "100" "L2433-V0" "" "" "MSINT" "Morgan Stanley Intrenational" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" "51980" "" "" "DTCYUS33XXX"]
   ["2024-12-16" "FOP Delivery" "DWEB000074" "Beyond Meat Inc" "US08862E1091" "AAPL US" "" "2024-12-16" "2024-12-16" "2024-12-16" "" "4500" "GBP" "4.52" "8428" "Non-Cash" "100" "L2433-V2" "" "" "MSINT" "Morgan Stanley Intrenational" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" "0" "" "" "DTCYUS33XXX"]
   ["2024-12-16" "FOP Delivery" "DWEB000074" "Beyond Meat Inc" "US08862E1092" "SPY US" "" "2024-12-16" "2024-12-16" "2024-12-16" "" "4500" "GBP" "4.52" "8428" "Non-Cash" "100" "L2433-V3" "" "" "MSINT" "Morgan Stanley Intrenational" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" "51" "" "" "DTCYUS33XXX"]
   ["2024-12-16" "FOP Delivery" "DWEB000074" "Beyond Meat Inc" "US08862E1091" "BYND US" "" "2024-12-16" "2024-12-16" "2024-12-16" "" "4500" "GBP" "4.52" "8428" "Non-Cash" "100" "L2433-V4" "" "" "MSINT" "Morgan Stanley Intrenational" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" "1" "" "" "DTCYUS33XXX"]
   ["2024-12-16" "FOP Delivery" "DWEB000074" "Beyond Meat Inc" "US08862E1092" "SPY US" "" "2024-12-16" "2024-12-16" "2024-12-16" "" "10" "GBP" "4.52" "8428" "Non-Cash" "100" "L2433-V5" "" "" "MSINT" "Morgan Stanley Intrenational" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" "5190" "" "" "DTCYUS33XXX"]
   ["2024-12-16" "FOP Delivery" "DWEB000074" "Beyond Meat Inc" "US08862E1092" "SPY US" "" "2024-12-16" "2024-12-16" "2024-12-16" "" "20" "GBP" "4.52" "8428" "Non-Cash" "100" "L2433-V6" "" "" "MSINT" "Morgan Stanley Intrenational" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" "501" "" "" "DTCYUS33XXX"]])

(defn write-csv [filename data]
  (with-open [w (io/writer filename)]
    (doseq [row data]
      (.write w (str/join "," row))
      (.newLine w))))

(defn -main []
  (write-csv "output.csv" data))

