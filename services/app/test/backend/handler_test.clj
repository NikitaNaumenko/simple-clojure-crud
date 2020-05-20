(ns backend.handler-test
  (:require [clojure.test :refer :all]
            [backend.models.patient :as db]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [backend.helpers :refer :all]))

(use-fixtures :each
  (fn [f]
    (create-patient)
    (f)
    (remove-patients)))

(deftest index-test
  (is (= 200
         (:status (test-app (mock/request :get "/"))))))

(deftest show-test
  (let [[patient] (get-patient-by-insurance-number insurance-number)
        patient_path (str "/patients/" (patient :patients/id))]
    (is (= 200
           (:status (test-app (mock/request :get patient_path)))))))

(deftest edit-test
  (let [[patient] (get-patient-by-insurance-number insurance-number)
        patient_path (str "/patients/" (patient :patients/id) "/edit")]
    (is (= 200
           (:status (test-app (mock/request :get patient_path)))))))

(defn create-patient-request []
  (test-app (-> (mock/request :post "/patients")
                (mock/json-body {"patient" {"full_name" "Test"
                                            "date_of_birth" "2020-02-02"
                                            "gender" "male"
                                            "address" "Bar"
                                            "health_insurance_number" "12345678"}}))))
(deftest create-test
  (let [{body :body} (create-patient-request)
        {patient :patient} (json/decode body true)
        patient_path (str "/patients/" (patient :patients/id))]
    (is (= 200
           (:status (test-app (mock/request :get patient_path)))))))

(deftest update-test
  (let [[patient] (get-patient-by-insurance-number insurance-number)
        patient_path (str "/patients/" (patient :patients/id))]
    (is (= 200
           (:status (test-app
                     (-> (mock/request :patch patient_path)
                         (mock/json-body {"patient" {"full_name" "Test"
                                                     "date_of_birth" "2020-02-02"
                                                     "gender" "male"
                                                     "address" "Bar"
                                                     "health_insurance_number" "12345678"}}))))))))

(deftest delete-test
  (let [[patient] (get-patient-by-insurance-number insurance-number)
        patient_path (str "/patients/" (patient :patients/id))]
    (test-app (mock/request :delete patient_path))
    (is (= 404
           (:status (test-app (mock/request :get patient_path)))))))
