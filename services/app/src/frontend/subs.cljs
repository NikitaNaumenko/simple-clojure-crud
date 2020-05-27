(ns frontend.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub :patients (fn [db _] (:patients db)))

(rf/reg-sub :active-page (fn [db _] (:active-page db)))

(rf/reg-sub :edited-patient
            (fn [db _] (when (:loaded-patient db) (:edited-patient db))))

(rf/reg-sub :loaded-patient (fn [db _] (:loaded-patient db)))
(rf/reg-sub :current_health_insurance_number
            (fn [db _] (:current_health_insurance_number db)))


(rf/reg-sub :show-patient (fn [db _] (:patient db)))

(rf/reg-sub :flash-message (fn [db _] (:flash-message db)))
(rf/reg-sub :filter (fn [db _] (get db :filter)))
(rf/reg-sub :new-patient (fn [db _] (get db :new-patient)))
(rf/reg-sub :health-insurance-number-exists?
            (fn [db _] (get db :health-insurance-number-exists?)))

(rf/reg-sub :errors (fn [db _] (get db :errors)))
