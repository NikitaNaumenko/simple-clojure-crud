(ns frontend.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :patients
 (fn [db [_ patients]]
   (get-in db [:patients patients])))

(rf/reg-sub
 :active-page
 (fn [db _]
   (:active-page db)))
