(ns backend.db
  (:require [next.jdbc :as jdbc]))

(def prod-config
  {:dbtype "postgres",
   :dbname (System/getenv "DB_NAME"),
   :host (System/getenv "DB_HOSTNAME"),
   :password (System/getenv "DB_PASSWORD"),
   :port (System/getenv "DB_PORT"),
   :user (System/getenv "DB_USERNAME")})

(def dev-config
  {:dbtype "postgres",
   :dbname (System/getenv "DB_NAME"),
   :host (System/getenv "DB_HOSTNAME"),
   :password (System/getenv "DB_PASSWORD"),
   :port (System/getenv "DB_PORT"),
   :user (System/getenv "DB_USERNAME")})

(def test-config
  {:dbtype "postgres",
   :dbname "test-db",
   :host "0.0.0.0",
   :port "5432",
   :user "postgres",
   :password ""})

; (def db-params
;   (cond
;     (= (System/getenv "CLJ_ENV") "production") db-config
;     (= (System/getenv "CLJ_ENV") "development") db-config
;     (= (System/getenv "CLJ_ENV") "test") test-config))

(def db prod-config)
(def db-dev dev-config)
(def db-test test-config)

(defn ds [] (jdbc/get-datasource db))

(defn ds-test [] (jdbc/get-datasource db-test))

(defn ds-dev [] (jdbc/get-datasource db-dev))
