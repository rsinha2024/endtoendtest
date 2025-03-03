(ns api.loanet_db
  (:require [clojure.java.jdbc :as jdbc]
            [util.properties :as p]
            )
  (:import (java.time LocalDate)
           (java.sql Date)))


(def db-spec
  {:dbtype (p/prop "DB_TYPE")
   :host (p/prop "DB_HOST")
   :port (p/prop "DB_PORT")
   :dbname (p/prop "DB_NAME")
   :user (p/prop "DB_USER")
   :password (p/prop "DB_PASSWORD")})


(defn delete-loanet-records [trade_date]
  (jdbc/with-db-connection [conn db-spec]
                           (jdbc/execute! conn
                                          ["DELETE FROM agent_lending.loanet_open_loans WHERE trade_date = CAST(? AS DATE)" trade_date])))

(defn loanet-records-exist [trade-date]
  (jdbc/with-db-connection [conn db-spec]
                           (let [query "SELECT COUNT(*) FROM agent_lending.loanet_open_loans WHERE trade_date = CAST(? AS DATE)"
                                 result (jdbc/query conn [query trade-date])
                                 ]
                               (>  (:count (first result))  0))
                                         ))

