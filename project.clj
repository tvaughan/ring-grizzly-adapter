(defproject tvaughan/ring-grizzly-adapter "0.1.0-SNAPSHOT"
  :description "Ring Grizzly adapter."
  :url "http://github.com/tvaughan/ring-grizzly-adapter"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-core "1.2.2"]
                 [ring/ring-servlet "1.2.2"]
                 [org.glassfish.grizzly/grizzly-http-servlet-server "2.3.12"]]
  :profiles
  {:dev {:dependencies [[clj-http "0.9.1"]]}})
