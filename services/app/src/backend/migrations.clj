(ns backend.migrations
  (:require [migratus.core :as migratus]))

(def db-config
  {:classname (System/getenv "DB_CLASSNAME"),
   :subprotocol (System/getenv "DB_SUBPROTOCOL"),
   :subname (System/getenv "DB_SUBNAME"),
   :user (System/getenv "DB_USERNAME"),
   :password (System/getenv "DB_PASSWORD")})

(def test-config
  {:classname (or (System/getenv "DB_CLASSNAME") "org.postgresql.Driver"),
   :subprotocol "postgres",
   :subname "//0.0.0.0:5432/test-db",
   :user "postgres",
   :password ""})

(def db-params
  (cond (= (System/getenv "CLJ_ENV") "production") db-config
        (= (System/getenv "CLJ_ENV") "development") db-config
        (= (System/getenv "CLJ_ENV") "test") test-config))

(def config {:store :database, :migration-dir "migrations/", :db db-params})

;apply pending migrations
(defn -main [] (migratus/migrate config))
