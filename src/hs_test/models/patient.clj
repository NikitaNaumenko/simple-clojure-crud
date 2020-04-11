(ns hs-test.models.patient
    (:require [next.jdbc.sql :as sql]
             [hs-test.db :refer :all]))

(defn create-patient [{:strs [full_name date_of_birth gender address health_insurance_number]}]
  (sql/insert! ds :patients {:full_name full_name
                             ; :date_of_birth date_of_birth
                             :gender gender
                             :address address
                             :health_insurance_number health_insurance_number}))
(defn get-patients []
  (println (System/getenv "DB_HOSTNAME"))
  (sql/query ds ["SELECT * FROM patients"]))

(defn get-patient [id]
  (sql/get-by-id ds :patients id))
