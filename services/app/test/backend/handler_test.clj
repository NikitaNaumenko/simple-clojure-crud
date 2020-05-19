(ns backend.handler-test
  (:require [clojure.test :refer :all]
            [backend.handler :refer [app]]
            [backend.models.patient :as db]
            [ring.mock.request :as mock]
            [backend.helpers :refer :all]
            [backend.db :as db-connection]))

(use-fixtures :each
              (fn [f]
                (create-patient)
                (f)
                (remove-patients)))

(deftest index-test
  (is (= 200
        (:status ((app (db-connection/ds-test)) (mock/request :get "/"))))))

(deftest show-test
  (let [[patient] (db/find-by (db-connection/ds-test) {:health_insurance_number insurance-number})]
    (is (= 200
           (:status ((app (db-connection/ds-test))
                     (mock/request :get (str "/patients/" (patient :patients/id)))))))))

(deftest edit-test
  (let [[patient] (db/find-by (db-connection/ds-test) {:health_insurance_number insurance-number})]
    (is (= 200
          (:status ((app (db-connection/ds-test))
                    (mock/request :get
                                  (str "/patients/"
                                       (patient :patients/id)
                                       "/edit"))))))))

(deftest create-test
  (is (= 200
         (:status ((app (db-connection/ds-test))
                   (-> (mock/request :post "/patients")
                           (mock/json-body {"patient" {"full_name" "Test"
                                                       "date_of_birth" "2020-02-02"
                                                       "gender" "male"
                                                       "address" "Bar"
                                                       "health_insurance_number" "12345678"}})))))))

(deftest update-test
  (let [[patient] (db/find-by (db-connection/ds-test) {:health_insurance_number insurance-number})]
    (is (= 200
         (:status ((app (db-connection/ds-test))
                   (-> (mock/request :patch (str "/patients/" (patient :patients/id)))
                       (mock/json-body {"patient" {"full_name" "Test"
                                                   "date_of_birth" "2020-02-02"
                                                   "gender" "male"
                                                   "address" "Bar"
                                                   "health_insurance_number" "12345678"}}))))))))
