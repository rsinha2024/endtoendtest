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



                 ;;
                 ;;
                 ;;
                 [io.cucumber/cucumber-java "7.11.0"]    ;; Cucumber Java dependency
                 [io.cucumber/cucumber-spring "7.11.0"]  ;; Optional, for integration with Spring (if needed)
                 ;; https://mvnrepository.com/artifact/io.cucumber/cucumber-junit-platform-engine
                 [io.cucumber/cucumber-junit-platform-engine "7.14.1"]
                 ;; https://mvnrepository.com/artifact/io.cucumber/cucumber-junit
                 ;; https://mvnrepository.com/artifact/io.cucumber/cucumber-junit
                 [io.cucumber/cucumber-junit "7.20.1"]
                 ;; https://mvnrepository.com/artifact/io.cucumber/cucumber-picocontainer
                 [io.cucumber/cucumber-picocontainer "7.20.1"]


                 ;; https://mvnrepository.com/artifact/io.cucumber/cucumber-bom
                 [io.cucumber/cucumber-bom "7.14.0" :extension "pom"]

                 ;; https://mvnrepository.com/artifact/org.clojure/test.check
                 [org.clojure/test.check "1.1.1"]
                 [org.junit.jupiter/junit-jupiter-api "5.7.2"]  ;; Add JUnit 5 if it's not included
                 [org.junit.jupiter/junit-jupiter-engine "5.7.2"]

                 ]

  :test-paths ["test"]
  :main ^:skip-aot endtoenedtest.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
