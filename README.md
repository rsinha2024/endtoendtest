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

To create a new test do the following
1)Create a new Gherkin feature file under test/features.
2)Copy one of the *.clj test files under test and rename it 
  In the last line change the "*.feature" reference to point to your own
3)remove all code between the square brackets in the def steps [] section
4)Execute it from the IDE
5)The cucumber/burpless plugin will generate the steps cprresponding to the 
 gherkin file
6)Implement the steps!





