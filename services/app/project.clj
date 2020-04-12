(defproject hs-test "0.1.0"
  :description "Test app for Health Samurai"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [seancorfield/next.jdbc "1.0.409"]
                 [org.postgresql/postgresql "42.2.12.jre7"]
                 [migratus "1.2.7"]
                 [ring/ring-core "1.8.0"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [ring/ring-mock "0.4.0"]
                 [ring/ring-jetty-adapter "1.8.0"]]
  :main hs-test.handler
  :plugins [[migratus-lein "0.5.0"]
            [lein-ring "0.12.5"]]
  :ring {:handler hs-test.handler/app}
  :migratus {:store :database
           :migration-dir "migrations"
           :db {:classname (System/getenv "DB_CLASSNAME")
                :subprotocol (System/getenv "DB_SUBPROTOCOL")
                :subname (System/getenv "DB_SUBNAME")
                :user (System/getenv "DB_USERNAME")
                :password (System/getenv "DB_PASSWORD")}}
  :profiles
  {:test {:prep-tasks [[ "migratus" "migrate"]]}})
