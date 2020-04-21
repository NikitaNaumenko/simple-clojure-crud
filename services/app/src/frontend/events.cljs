(ns frontend.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.http-fx]
            [frontend.db :as db]
            [ajax.core :as ajax]))

(reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(reg-event-fx
 :set-active-page
 (fn [{:keys [db]} [_ {:keys [page]}]]
   (let [set-page (assoc db :active-page page)]
     (case page
       :home {:db set-page
              :dispatch-n  (list [:get-patients])}
       :patients {:db set-page
              :dispatch-n  (list [:get-patients])}))))

(reg-event-fx
  :get-patients
  (fn [db [_ patients]]
    {:http-xhrio {:method :get
                  :uri "/patients"
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [::success]
                  :on-failure [::failure]}}))

(reg-event-db ::success
  (fn [db [_ result]]
    (into db {:patients result})))


(reg-event-db ::failure
  (fn [db [_ _result]]
    (into db {:patients []})))
