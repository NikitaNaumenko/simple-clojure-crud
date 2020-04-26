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

; Navigate on page
(reg-event-fx
 :set-active-page
 (fn [{:keys [db]} [_ {:keys [page id]}]]
   (let [set-page (assoc db :active-page page)]
     (case page
       :home {:db set-page }
       :patients {:db set-page
                  :dispatch-n  (list [:get-patients])}
       :edit-patient {:db (assoc set-page :edit-patient id)
                      :dispatch [:edit-patient {:id id}]}
       :new-patients {:db set-page}))))

(reg-event-fx
  :get-patients
  (fn [db [_ params]]
    (println params)
    {:http-xhrio {:method :get
                  :uri "/patients"
                  :url-params params
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:get-patients-success]
                  :on-failure [::failure]}}))

(reg-event-db :get-patients-success
  (fn [db [_ {patients :patients}]]
    (assoc db :patients patients)))

(reg-event-db ::failure
  (fn [{:keys [db]} [_ _]]
    (assoc db :patients [])))

(reg-event-fx
  :create-patient
  (fn [db [_ params]]
    {:http-xhrio {:method :post
                  :uri "/patients"
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :params {:patient params}
                  :on-success [:create-patient-success]
                  :on-failure [:failure]}}))

(reg-event-fx
 :create-patient-success
 (fn [{patient :db} [{props :patient}]]
   {:db (merge patient props)
    :set-hash {:hash "/"}}))

(reg-event-fx
  :edit-patient
  (fn [db [_ {:keys [id]}]]
    (println "2")
    {:http-xhrio {:method :get
                  :uri (str "/patients/" id "/edit")
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:edit-patient-success]
                  :on-failure [::failure]}}))

(reg-event-db :edit-patient-success
  (fn [db [_ {patient :patient}]]
    (println patient)
    (assoc db :edited-patient patient)))
