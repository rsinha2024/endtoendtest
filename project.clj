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
                 [com.amazonaws/aws-java-sdk-dynamodb "1.12.132"] ;; AWS DynamoDB SDK
                 [amazonica "0.3.151"] ;; Amazonica library
                 ;; https://mvnrepository.com/artifact/org.clojure/data.json
                 [org.clojure/data.json "2.5.1"]
                 ;; https://mvnrepository.com/artifact/clojure.java-time/clojure.java-time
                 [clojure.java-time/clojure.java-time "1.4.3"]
                 [net.clojars.danielmiladinov/burpless "0.1.0"] ;; Add Burpless dependency
                 [org.apache.poi/poi-ooxml "5.2.3"]
                 [org.clojure/data.csv "1.0.0"]
                 [org.apache.sshd/sshd-sftp "2.7.0"]
                 ]

  :test-paths ["test"]
  :main ^:skip-aot endtoenedtest.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :plugins [[lein-midje "3.2"]])
