(defproject hs-test "0.1.0-SNAPSHOT"
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
                 [ring/ring-jetty-adapter "1.8.0"]]
  :plugins [[migratus-lein "0.5.0"]
            [lein-ring "0.12.5"]]
  :ring {:handler hs-test.handler/app}
  :migratus {:store :database
           :migration-dir "migrations"
           :db {:classname "org.postgresql.Driver"
                :subprotocol "postgres"
                :subname "//db:5432/hs-dev"
                :user "docker"
                :password "docker"
                }}
  :profiles
  {:test {:prep-test [[ "migratus" "migrate"]]}})
