(defproject clj-api "0.0.1-SNAPSHOT"
  :description "API developed with pedestal"
  :license {:name "UNLICENSED"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.5.8"]
                 [io.pedestal/pedestal.jetty "0.5.8"]
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.26"]
                 [org.slf4j/jcl-over-slf4j "1.7.26"]
                 [org.slf4j/log4j-over-slf4j "1.7.26"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config"]
  :source-paths ["source"]
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "api.server/run-dev"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.5.8"]]}
             :uberjar {:aot [api.server]}}
  :main ^{:skip-aot true} api.server)
