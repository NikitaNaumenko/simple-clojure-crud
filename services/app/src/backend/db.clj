(ns backend.db
  (:require [next.jdbc :as jdbc]))

(def db {:dbtype "postgres"
         :dbname (System/getenv "DB_NAME")
         :host (System/getenv "DB_HOSTNAME")
         :password (System/getenv "DB_PASSWORD")
         :port (System/getenv "DB_PORT")
         :user (System/getenv "DB_USERNAME")})

(def ds (jdbc/get-datasource db))
