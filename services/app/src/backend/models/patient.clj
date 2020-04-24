(ns backend.models.patient
  (:require [backend.db :refer [ds]]
            [backend.utils :as utils]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]))

(defn create-patient [{:strs [full_name date_of_birth gender address health_insurance_number]}]
  (let [parsed_date (utils/parse-date date_of_birth)]
   (sql/insert! ds :patients {:full_name full_name
                              :date_of_birth (java.util.Date.)
                               :gender gender
                              :address address
                              :health_insurance_number health_insurance_number} )))


(create-patient {"full_name" "Nikita" "date_of_birth" "1993-12-11" "gender" "male" "address" "Jopa" "health_insurance_number" "431"})

(defn get-patients []
  (sql/query ds ["SELECT * FROM patients"] {:builder-fn rs/as-unqualified-lower-maps}))
(clojure.core/count (get-patients))

(defn get-patient [id]
  (sql/get-by-id ds :patients id))

(defn find-by [params]
  (sql/find-by-keys ds :patients params))

(defn update-patient [{:strs [full_name date_of_birth gender address health_insurance_number] id :id}]
  (let [parsed_date (utils/parse-date date_of_birth)]
    (let [patient_id (utils/parse-int id)]
      (sql/update! ds :patients {:full_name full_name
                                 :date_of_birth parsed_date
                                 :gender gender
                                 :address address
                                 :health_insurance_number health_insurance_number}
                   {:id patient_id}))))

(defn delete-patients []
  (jdbc/execute! ds ["DELETE FROM patients"]))