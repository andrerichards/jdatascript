(defproject defng/jdatascript "0.5.0"
  :description "Java wrapper for DataScript."
  :url "https://github.com/andrerichards/jdatascript"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojars.defng/datascript "0.16.3"]
                 [defng/joculer "0.4.0"]
                 [junit/junit "4.12"]]
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"])
