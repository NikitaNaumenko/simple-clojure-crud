(ns hs-test.models.patient
    (:require [next.jdbc.sql :as sql]
             [hs-test.db :refer :all]))

(defn create-patient [patient]
  (sql/insert! ds :patients patient ))
(defn get-patients []
  (sql/query ds ["SELECT * FROM patients"]))

(defn get-patient [id]
  (sql/get-by-id ds :patients id))
