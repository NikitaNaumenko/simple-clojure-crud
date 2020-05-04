(ns frontend.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
  :patients
  (fn [db _]
    (:patients db)))

(rf/reg-sub
  :active-page
  (fn [db _]
    (:active-page db)))

(rf/reg-sub
  :edited-patient
  (fn [db _]
    (when (:loaded-patient db)
     (:edited-patient db) ))) 

(rf/reg-sub
  :loaded-patient
  (fn [db _]
    (:loaded-patient db)))


(rf/reg-sub
  :show-patient
  (fn [db _]
    (:patient db)))

(rf/reg-sub
  :flash-message
  (fn [db _]
    (:flash-message db)))
