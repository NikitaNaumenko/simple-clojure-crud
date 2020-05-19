(ns backend.models.patient
  (:require [backend.utils :as utils]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [next.jdbc.date-time :refer :all]
            [next.jdbc.result-set :as rs]))

(defn create-patient
  [ds {:strs [full_name date_of_birth gender address health_insurance_number]}]
  (let [parsed_date (utils/parse-date date_of_birth)]
    (sql/insert! ds
                 :patients
                 {:full_name full_name,
                  :date_of_birth parsed_date,
                  :gender gender,
                  :address address,
                  :health_insurance_number health_insurance_number})))

(defn get-patients
  ([ds]
   (sql/query ds
              ["SELECT * FROM patients"]
              {:builder-fn rs/as-unqualified-lower-maps}))
  ([ds params]
   (let [value (str "%" (params "query") "%")]
     (sql/query
       ds
       ["SELECT * FROM patients WHERE full_name ILIKE ? OR CAST(id AS TEXT) ILIKE ?"
        value value]
       {:builder-fn rs/as-unqualified-lower-maps}))))

(defn get-patient
  [ds id]
  (sql/get-by-id ds :patients id {:builder-fn rs/as-unqualified-lower-maps}))

(defn find-by [ds params] (sql/find-by-keys ds :patients params))

(defn update-patient
  [ds id
   {:strs [full_name date_of_birth gender address health_insurance_number]}]
  (let [parsed_date (utils/parse-date date_of_birth)
        patient_id (utils/parse-int id)]
    (sql/update! ds
                 :patients
                 {:full_name full_name,
                  :date_of_birth parsed_date,
                  :gender gender,
                  :address address,
                  :health_insurance_number health_insurance_number}
                 {:id patient_id})))

(defn destroy-patient [ds id] (sql/delete! ds :patients {:id id}))

(defn delete-patients [ds] (jdbc/execute! ds ["DELETE FROM patients"]))
