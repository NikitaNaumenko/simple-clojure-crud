(ns hs-test.handler-test
   (:require [clojure.test :refer :all]
             [hs-test.handler :refer [app]]
             [hs-test.models.patient :as db]
             [hs-test.helpers :refer :all]))


(use-fixtures :each
              (fn [f]
                (create-patient)
                (f)
                (remove-patients)))

(deftest index-test
  (is (= 200
           (:status (test-request "/patients" app)))))

(deftest show-test
  (let [[patient] (db/find-by {:health_insurance_number insurance-number})]
   (is (= 200
           (:status (test-request (str "/patients/" (patient :patients/id)) app))))))

(deftest new-test
  (is (= 200
           (:status (test-request "/patients/new" app)))))

(deftest edit-test
  (println (db/find-by {:health_insurance_number insurance-number})) 
  (let [[patient] (db/find-by {:health_insurance_number insurance-number})]
    (println "POP")
    (println patient)
   (is (= 200
           (:status (test-request (str "/patients/" (patient :patients/id) "/edit") app))))))
