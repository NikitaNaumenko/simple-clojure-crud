(ns hs-test.db
    (:require [next.jdbc :as jdbc]))

(def db {:dbtype "postgres" :dbname "hs-dev"})
(def ds (jdbc/get-datasource db))
