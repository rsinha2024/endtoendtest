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

;; Generate the SQL query


(deftest first
  (testing "uppercase map"
    (println(generate-sql-query trade-data))
    (println "file name=" (gen/generate-file-name "2024-12-23"))
    (println "formatteddate=" (gen/convert-date-format "2024-12-23"))
    (println "current time=" (gen/current-time-hhmmss))
    (println ( map-to-string h))
    (is (= "NAME=John, AGE=30, CITY=New York"
           (map-to-string {:name "John", :age 30, :city "New York"})))))