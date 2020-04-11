(ns hs-test.db
    (:require [next.jdbc :as jdbc]))

(def db {:dbtype "postgres" :dbname "hs-dev" :host "db" :password "docker" :user "docker"})
(def ds (jdbc/get-datasource db))
