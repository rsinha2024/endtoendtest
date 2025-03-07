# endtoenedtest for AgentLending

Environment config is config.properties
The tests can be run in dev,qa,uat
Need to install 
1)https://leiningen.org/
2)Cursive plugin for Intellij
3)Cucumber plugin for Intellij
4)Gherkin plugin for Intellij

To compile and run
a)lein deps
b)lein test

To run in
a)Dev: Edit src/util/properties.clj and change line 17 as follows
(def properties (load-properties "resources/dev.config.properties"))

b)Qa: Edit src/util/properties.clj and change line 17 as follows
(def properties (load-properties "resources/qa.config.properties"))

c)Uat: Edit src/util/properties.clj and change line 17 as follows
(def properties (load-properties "resources/uat.config.properties"))

d)Locally
Edit src/util/properties.clj and change line 17 as follows
(def properties (load-properties "resources/config.properties"))






