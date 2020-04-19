(ns hs-test.handler-test
   (:require [clojure.test :refer :all]
             [hs-test.handler :refer [app]]
             [hs-test.models.patient :as db]
             [ring.mock.request :as mock]
             [hs-test.helpers :refer :all]))

(use-fixtures :each
              (fn [f]
                (create-patient)
                (f)
                (remove-patients)))

(deftest index-test
  (is (= 200
           (:status (app (mock/request :get "/patients" ))))))

(deftest show-test
  (let [[patient] (db/find-by {:health_insurance_number insurance-number})]
   (is (= 200
           (:status (app (mock/request :get (str "/patients/" (patient :patients/id)))))))))

(deftest new-test
  (is (= 200
           (:status (app (mock/request :get "/patients/new"))))))

(deftest edit-test
  (let [[patient] (db/find-by {:health_insurance_number insurance-number})]
   (is (= 200
           (:status (app (mock/request :get (str "/patients/" (patient :patients/id "/edit")))))))))

(deftest create-test
  (is (= 302
         (:status (app (-> (mock/request :post "/patients")
                           (mock/body {"full_name" "Test"
                                       "date_of_birth" "2020-02-02"
                                       "gender" "male"
                                       "address" "Bar"
                                       "health_insurance_number" "12345678"})))))))

(deftest update-test
  (let [[patient] (db/find-by {:health_insurance_number insurance-number})]
    (is (= 302
         (:status (app (-> (mock/request :patch (str "/patients/" (patient :patients/id)))
                           (mock/body {"full_name" "Test"
                                       "date_of_birth" "2020-02-02"
                                       "gender" "male"
                                       "address" "Bar"
                                       "health_insurance_number" "12345678"}))))))))
