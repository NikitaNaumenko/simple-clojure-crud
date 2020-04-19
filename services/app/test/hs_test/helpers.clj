(ns hs-test.helpers
  (:require [hs-test.models.patient :as db]))

(def insurance-number "12345")

(defn create-patient []
  (when (empty? (db/find-by {:health_insurance_number insurance-number}))
    (db/create-patient {"full_name" "Foo"
                        "date_of_birth" "2020-02-02"
                        "address" "Bar"
                        "gender" "male"
                        "health_insurance_number" insurance-number })))

(defn remove-patients []
  (db/delete-patients))

(defn test-request [resource web-app method & params]
  (web-app {:request-method method :uri resource :params params}))
 

