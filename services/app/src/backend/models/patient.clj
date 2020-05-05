(ns backend.models.patient
  (:require [backend.db :refer [ds]]
            [backend.utils :as utils]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [next.jdbc.date-time :refer :all]
            [next.jdbc.result-set :as rs]))

(defn create-patient [{:strs [full_name date_of_birth gender address health_insurance_number]}]
  (let [parsed_date (utils/parse-date date_of_birth)]
    (sql/insert! ds :patients {:full_name full_name
                               :date_of_birth parsed_date
                               :gender gender
                               :address address
                               :health_insurance_number health_insurance_number})))

(defn get-patients
  ([] (sql/query ds ["SELECT * FROM patients"] {:builder-fn rs/as-unqualified-lower-maps}))
  ([params]
    (let [value (str "%" (params "query") "%")]
     (println value)
      (sql/query ds
                 ["SELECT * FROM patients WHERE full_name ILIKE ? OR CAST(id AS TEXT) ILIKE ?" value value]
                 {:builder-fn rs/as-unqualified-lower-maps}))))

(defn get-patient [id]
  (sql/get-by-id ds :patients id {:builder-fn rs/as-unqualified-lower-maps}))

(defn find-by [params]
  (sql/find-by-keys ds :patients params))

(defn update-patient [id {:strs [full_name date_of_birth gender address health_insurance_number]}]
  (let [parsed_date (utils/parse-date date_of_birth)
        patient_id (utils/parse-int id)]
    (sql/update! ds :patients {:full_name full_name
                                 :date_of_birth parsed_date
                                 :gender gender
                                 :address address
                                 :health_insurance_number health_insurance_number}
                   {:id patient_id})))

(defn destroy-patient [id]
  (sql/delete! ds :patients {:id id}))

(defn delete-patients []
  (jdbc/execute! ds ["DELETE FROM patients"]))
