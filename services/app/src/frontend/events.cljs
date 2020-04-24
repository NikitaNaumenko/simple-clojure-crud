(ns frontend.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db reg-fx]]
            [day8.re-frame.http-fx]
            [frontend.db :as db]
            [ajax.core :as ajax])) 

(reg-fx
 :set-hash
 (fn [{:keys [hash]}]
   (set! (.-hash js/location) hash)))

(reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(reg-event-fx
 :set-active-page
 (fn [{:keys [db]} [_ {:keys [page]}]]
   (let [set-page (assoc db :active-page page)]
     (case page
       :home {:db set-page }
       :patients {:db set-page
                  :dispatch-n  (list [:get-patients])}
       :new-patients {:db set-page}))))

(reg-event-fx
  :get-patients
  (fn [db [_ patients]]
    {:http-xhrio {:method :get
                  :uri "/patients"
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [::success]
                  :on-failure [::failure]
                  :db (assoc-in db [:loading :patients] true)
                  }}))

(reg-event-db ::success
  (fn [db [_ {patients :patients}]]
    (-> db
         (assoc-in [:loading :patients] false)
         (assoc :patients patients))))

(reg-event-db ::failure
  (fn [{:keys [db]} [_ _]]
    (assoc db :patients [])))

(reg-event-fx
  :create-patient
  (fn [db [_ params]]
    (println params)
    {:http-xhrio {:method :post
                  :uri "/patients"
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :params {:patient params}
                  :on-success [:register-user-success]
                  :on-failure [:failure]}}))

(reg-event-fx
 :register-user-success
 (fn [{patient :db} [{props :patient}]]
   {:db (merge patient props)
    :set-hash {:hash "/"}}))

(reg-event-db ::failure
  (fn [{:keys [db]} [_ _]]
    (assoc db :patients [])))
