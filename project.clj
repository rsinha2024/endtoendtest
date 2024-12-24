(defproject endtoenedtest "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.0"]
                 [amazonica "0.3.153"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.postgresql/postgresql "42.2.5"]  ;; PostgreSQL JDBC driver (use the appropriate one for MySQL if needed)
                 [com.zaxxer/HikariCP "3.4.5"]  ;; Connection pool (optional but recommended)
                 ]


  :main ^:skip-aot endtoenedtest.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
