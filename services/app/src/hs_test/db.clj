(ns hs-test.db
    (:require [next.jdbc :as jdbc]))


(System/getenv "DB_HOSTNAME")
(def db {:dbtype "postgres"
         :dbname (System/getenv "DB_NAME")
         :host (System/getenv "DB_HOSTNAME")
         :password (System/getenv "DB_PASSWORD")
         :user (System/getenv "DB_USERNAME")})
(def ds (jdbc/get-datasource db))
