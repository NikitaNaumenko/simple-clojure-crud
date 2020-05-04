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
       :show-patient {:db (assoc set-page :show-patient id)
                      :dispatch [:show-patient {:id id}]}
       :new-patient {:db set-page}))))

(reg-event-fx
  :get-patients
  (fn [db [_ params]]
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
 (fn [{:keys [db]} _]
   {:db (assoc db :flash-message {:flash-type "success" :flash-message "Patient was created successfully"})
    :set-hash {:hash "/patients"}}))

(reg-event-fx
  :show-patient
  (fn [db [_ {:keys [id]}]]
    {:http-xhrio {:method :get
                  :uri (str "/patients/" id)
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:show-patient-success]
                  :on-failure [:failure]}}))

(reg-event-db :show-patient-success
  (fn [db [_ {patient :patient}]]
    (-> db
       (assoc :patient patient)
       (assoc :loaded-patient true))))

(reg-event-fx
  :edit-patient
  (fn [db [_ {:keys [id]}]]
    {:http-xhrio {:method :get
                  :uri (str "/patients/" id "/edit")
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:edit-patient-success]
                  :on-failure [:failure]}}))

(reg-event-db :edit-patient-success
  (fn [db [_ {patient :patient}]]
    (-> db
       (assoc :edited-patient patient)
       (assoc :loaded-patient true))))

(reg-event-fx
  :update-patient
  (fn [db [_ params id]]
    {:http-xhrio {:method :patch
                  :uri (str "/patients/" id)
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :params {:patient params}
                  :on-success [:update-patient-success]
                  :on-failure [:failure]}}))

(reg-event-fx
 :update-patient-success
 (fn [{:keys [db]} _]
   {:db (assoc db :flash-message {:flash-type "success" :flash-message "Patient was updated successfully"})
    :set-hash {:hash "/patients"}}))

(reg-event-fx
  :delete-patient
  (fn [db [_ id]]
    {:http-xhrio {:method :delete
                  :uri (str "/patients/" id)
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:delete-patient-success]
                  :on-failure [:failure]}}))

(reg-event-db :delete-patient-success
  (fn [db [_ _]]
       {:db (assoc db :flash-message {:flash-type "success" :flash-message "Patient was deleted successfully"})
        :set-hash {:hash "/patients"}}))
