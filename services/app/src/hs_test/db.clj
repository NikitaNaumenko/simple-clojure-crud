(ns hs-test.db
    (:require [next.jdbc :as jdbc]))


(def db {:dbtype "postgres"
         :dbname (System/getenv "DB_NAME")
         :host (System/getenv "DB_HOSTNAME")
         :password (System/getenv "DB_PASSWORD")
         :port (System/getenv "DB_PORT")
         :user (System/getenv "DB_USERNAME")})

; (def db {:jdbcUrl "jdbc:postgresql://doadmin:rrzhdm8rmz8rk0mi@db-postgresql-fra1-24734-do-user-4986171-0.a.db.ondigitalocean.com:25060/defaultdb?sslmode=require"})
(def ds (jdbc/get-datasource db))

(println db)
