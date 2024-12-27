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
(deftest first
  (testing "uppercase map"
    (read-resource-file)
    (gen/generate_file (p/prop "SGDOWNLOAD_INPUT_JSON") "2024-12-27" )
    (println "file name=" (gen/generate-file-name "2024-12-23"))
    (println "formatteddate=" (gen/convert-date-format "2024-12-23"))
    (println "current time=" (gen/current-time-hhmmss))
    (println ( map-to-string h))
    (is (= "NAME=John, AGE=30, CITY=New York"
           (map-to-string {:name "John", :age 30, :city "New York"})))))