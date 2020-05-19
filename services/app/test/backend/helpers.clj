(ns backend.helpers
  (:require [backend.models.patient :as db]
            [backend.db :refer [ds-test]]))

(def insurance-number "12345")

(defn create-patient []
  (when (empty? (db/find-by (ds-test) {:health_insurance_number insurance-number}))
    (db/create-patient (ds-test) {"full_name" "Foo"
                        "date_of_birth" "2020-02-02"
                        "address" "Bar"
                        "gender" "male"
                        "health_insurance_number" insurance-number })))

(defn remove-patients []
  (db/delete-patients (ds-test)))
