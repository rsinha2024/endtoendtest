(ns test
  (:require [clojure.test :refer :all]
            [file.generator.trade-continous-map :as gen]
            [util.properties :as p]
            [clojure.java.io :as io]
            ))
(def h {:dbtype "postgres"
        :host "dwdev-aurora-babelfish-instance-1.csefnakly5ig.us-east-1.rds.amazonaws.com"
        :port 5432
        :dbname "post_trade"
        :user "cost_basis_user"
        :password "@sDfgKJ7g^%g2ug"})
(defn map-to-string [m]
  (clojure.string/join "\n"
                       (map (fn [[k v]] (str (clojure.string/upper-case (name k)) "=" (str v))) m)))

(defn read-resource-file []
  (let [resource-path "output/DW.TRADE_INSTRUCTION.CONTINUOUS.20241227.161901.csv"]  ;; Path relative to 'resources'
    (if-let [resource (io/resource resource-path)]
      (with-open [rdr (io/reader resource)]
        (slurp rdr))
      (println "Resource not found!"))))
(defn extract-trade-ids [data]
  "Extract trade ids from a sequence of vectors at position 18."
  (map #(nth % 17) data))  ;; nth gets the value at index 18

(defn generate-sql-query [data]
  "Generate an SQL query with trade_id values from a sequence of vectors."
  (let [trade-ids (extract-trade-ids data)
        formatted-ids (clojure.string/join "," (map #(str "'" % "'") trade-ids))]   ;; Format IDs as a comma-separated string
    (str "SELECT * FROM agent_lending.agent_loan_requests WHERE trade_id IN (" formatted-ids ")")))

;; Example data: sequence of vectors, where trade-id is at index 18
(def trade-data [
                 ["2024-12-27" "FOP Delivery" "DWEB000074" "Beyond Meat Inc" "US08862E1091" "BYND US" "" "2024-12-27" "2024-12-27" "2024-12-27" "" 5000 "GBP" 4.52 8428 "Non-Cash" 100 "L2433-V1" "" "" "MSINT" "Morgan Stanley International" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" 5198 "" "" "DTCYUS33XXX"]
                 ["2024-12-27" "FOP Delivery" "DWEB000074" "Beyond Meat Inc" "US08862E1091" "BYND US" "" "2024-12-27" "2024-12-27" "2024-12-27" "" 4500 "GBP" 4.52 8428 "Non-Cash" 100 "L2433-V0" "" "" "MSINT" "Morgan Stanley International" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" 51980 "" "" "DTCYUS33XXX"]
                 ["2024-12-27" "FOP Delivery" "DWEB000074" "Beyond Meat Inc" "US08862E1091" "AAPL US" "" "2024-12-27" "2024-12-27" "2024-12-27" "" 4500 "GBP" 4.52 8428 "Non-Cash" 100 "L2433-V2" "" "" "MSINT" "Morgan Stanley International" "MLILGB3LESF" "" "" "12B-34567" "Merill Lynch NY" "" "DTCYID" 0 "" "" "DTCYUS33XXX"]
                 ])
(def row1 ["11/20/24"     10            25.26          "USD"            0.788519    19.91799     0          "GBP"          "0.45"               "40000"           90            365            75                0                 0           0                 ""           0.0                   "FREETRAD"       "Freetrade Ltd"      "FRVV000001"              ""                   "MSINT"       "Morgan Stanley International Equity"    "L30178"      "Closed"      "Open-End"    "Non-Cash"         100                     105     "11/18/24"   "11/18/24"        "11/18/24"        "11/20/24" "Nano Nuclear Energy Inc" "US63010H1086" "NNE US"            "NNE US"        "BBG01F7QNWC1" ""                  "Equity"          "US"                        "US"
           ])
(def row2 ["11/20/24"    1518           27.02          "USD"            0.788519    21.305783   32342.18    "GBP"           ""                   ""               4.5           365            75               3.987392         0.996848     2.990544                                              "FREETRAD" "Freetrade Ltd" "FRVV000001" "" "BAML" "Bank of America Merrill Lynch" "L30440" "Open" "Open-End" "Non-Cash" 100 105 "11/20/24" "11/20/24" "11/20/24" "" "Nuscale Power Corp" "US67079K1007" "SMR US" "SMR US" "BBG00YG48NM6" "" "Equity" "KY" "US"]
  )
(def body ["2024-09-16,15000,20.650000,USD,0.762079,15.736931,236053.97,GBP,,,,,0.280000%,365,80.00%,1.810825,0.362165,1.448660,FREETDSB,Freetrade Limited Sandbox,FTDWACC123,,MSINT,Morgan Stanley International,L2438,Open,Open-End,Non-Cash,100.000,105,2024-09-16,2024-09-16,2024-09-16,,Gamestop Corp-class A,US36467W1099,GME US,GME US,BBG000BB5BF6,,Equity,US,US"
           ])
;; Generate the SQL query
(def data ["lending_rate"
           "day_count_basis"
           "fee_split"
           "accrual_gross"
           "accrual_agent"
           "accrual_net"
           "lender_code"
           "lender_name"
           "lender_account_number"
           "ext_borrower_code"
           "borrower_code"
           "borrower_name"
           "loan_id"
           "loan_status"
           "loan_type"
           "collateral_type"
           "withholding_tax_rate"
           "margin"
           "trade_date"
           "collateral_date"
           "settlement_date"
           "end_date"
           "instrument_name"
           "isin"
           "composite_ticker"
           "country_ticker"
           "bbgid"
           "ext_instrument_id"
           "instrument_type"
           "country_of_incorporation"
           "country_of_listing"])

(deftest first
  (testing "uppercase map"
    (println "row count =" (count row2))
    (println(generate-sql-query trade-data))
    (println "file name=" (gen/generate-file-name "2024-12-23"))
    (println "formatteddate=" (gen/convert-date-format "2024-12-23"))
    (println "current time=" (gen/current-time-hhmmss))
    (println ( map-to-string h))
    (is (= "NAME=John, AGE=30, CITY=New York"
           (map-to-string {:name "John", :age 30, :city "New York"})))))