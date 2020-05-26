(ns frontend.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db reg-fx]]
            [day8.re-frame.http-fx]
            [frontend.db :as db]
            [clojure.string :as str]
            [ajax.core :as ajax]))

(defn url-encode [x] (js/encodeURIComponent x))

(defn gen-query-string
  [params]
  (->> params
       (map (fn [[k v]]
              (cond (set? v) (str (name k) "=" (str/join "," v))
                    :else (str (name k) "=" (url-encode v)))))
       (str/join "&")
       (str "?")))

(defn build-path
  [db]
  (let [current-path (str "/" (name (:active-page db)))
        query-string (gen-query-string (select-keys db [:filter]))]
    (str current-path query-string)))

(reg-event-fx :set-hash
              (fn [params]
                (let [path (build-path (:db params))]
                  (set! (.-hash js/location) path))))

(reg-event-fx :filter-table
              (fn [{db :db} [_ q]]
                {:dispatch-debounce
                   {:event [:set-hash {:q q}], :delay 500, :key :tables-search},
                 :db (assoc db :filter q)}))

(reg-event-fx :change-new-patient
              (fn [{db :db} [_ value key]]
                (let [changed-patient (assoc (:new-patient db) key value)]
                  {:db (assoc db :new-patient changed-patient)})))

(reg-event-fx :change-edited-patient
              (fn [{db :db} [_ value key]]
                (let [changed-patient (assoc (:edited-patient db) key value)]
                  {:db (assoc db :edited-patient changed-patient)})))

(reg-event-fx
  :change-patient-insurance-number
  (fn [{db :db} [_ value entity-key]]
    (let [changed-patient (assoc (entity-key db)
                            :health_insurance_number value)]
      {:db (assoc db entity-key changed-patient),
       :dispatch-debounce {:event [:validate-exists-insurance-number value],
                           :delay 300,
                           :key :health-insurance-number-exists?}})))

(reg-event-fx
  :validate-exists-insurance-number
  (fn [{db :db} [_ value]]
    {:http-xhrio {:method :get,
                  :uri "/validate-exists",
                  :url-params {:health_insurance_number value,
                               :attr :health_insurance_number},
                  :format (ajax/json-request-format),
                  :response-format (ajax/json-response-format {:keywords?
                                                                 true}),
                  :on-success [:validate-exists-insurance-number-success],
                  :on-failure [::failure]}}))

(reg-event-fx :validate-exists-insurance-number-success
              (fn [{:keys [db]} [_ [{exists :exists}]]]
                {:db (assoc db :health-insurance-number-exists? exists)}))

(reg-event-fx
  :set-active-page
  (fn [{:keys [db]} [_ {:keys [page id]}]]
    (let [set-page (assoc db :active-page page)]
      (case page
        :home {:db set-page}
        :patients {:db set-page, :dispatch-n (list [:get-patients])}
        :edit-patient {:db (assoc set-page :edit-patient id),
                       :dispatch [:edit-patient {:id id}]}
        :show-patient {:db (assoc set-page :show-patient id),
                       :dispatch [:show-patient {:id id}]}
        :new-patient {:db set-page, :dispatch [:init-new-patient]}))))

(reg-event-fx :get-patients
              (fn [{db :db} [_ params]]
                {:http-xhrio {:method :get,
                              :uri "/patients",
                              :url-params (select-keys db [:filter]),
                              :format (ajax/json-request-format),
                              :response-format (ajax/json-response-format
                                                 {:keywords? true}),
                              :on-success [:get-patients-success],
                              :on-failure [::failure]}}))

(reg-event-db :init-new-patient
              (fn [db _]
                (assoc db
                  :new-patient {:full_name "",
                                :gender "",
                                :date_of_birth "",
                                :address "",
                                :health_insurance_number ""})))
(reg-event-db :get-patients-success
              (fn [db [_ {patients :patients}]] (assoc db :patients patients)))

(reg-event-db ::failure (fn [{:keys [db]} [_ _]] (assoc db :patients [])))

(reg-event-fx :create-patient
              (fn [_ [_ params]]
                {:http-xhrio {:method :post,
                              :uri "/patients",
                              :format (ajax/json-request-format),
                              :response-format (ajax/json-response-format
                                                 {:keywords? true}),
                              :params {:patient params},
                              :on-success [:create-patient-success],
                              :on-failure [:failure]}}))

(reg-event-fx
  :create-patient-success
  (fn [{:keys [db]} _]
    {:db (assoc db
           :flash-message {:flash-type "success",
                           :flash-message "Patient was created successfully"}),
     :set-hash {:hash "/patients"}}))

(reg-event-fx :show-patient
              (fn [_ [_ {:keys [id]}]]
                {:http-xhrio {:method :get,
                              :uri (str "/patients/" id),
                              :format (ajax/json-request-format),
                              :response-format (ajax/json-response-format
                                                 {:keywords? true}),
                              :on-success [:show-patient-success],
                              :on-failure [:failure]}}))

(reg-event-db :show-patient-success
              (fn [db [_ {patient :patient}]]
                (-> db
                    (assoc :patient patient)
                    (assoc :loaded-patient true))))

(reg-event-fx :edit-patient
              (fn [_ [_ {:keys [id]}]]
                {:http-xhrio {:method :get,
                              :uri (str "/patients/" id "/edit"),
                              :format (ajax/json-request-format),
                              :response-format (ajax/json-response-format
                                                 {:keywords? true}),
                              :on-success [:edit-patient-success],
                              :on-failure [:failure]}}))

(reg-event-db :edit-patient-success
              (fn [db [_ {patient :patient}]]
                (-> db
                    (assoc :edited-patient patient)
                    (assoc :loaded-patient true))))

(reg-event-fx :update-patient
              (fn [_ [_ params id]]
                {:http-xhrio {:method :patch,
                              :uri (str "/patients/" id),
                              :format (ajax/json-request-format),
                              :response-format (ajax/json-response-format
                                                 {:keywords? true}),
                              :params {:patient params},
                              :on-success [:update-patient-success],
                              :on-failure [:failure]}}))

(reg-event-fx
  :update-patient-success
  (fn [{:keys [db]} _]
    {:db (assoc db
           :flash-message {:flash-type "success",
                           :flash-message "Patient was updated successfully"}),
     :set-hash {:hash "/patients"}}))

(reg-event-fx :delete-patient
              (fn [_ [_ id]]
                {:http-xhrio {:method :delete,
                              :uri (str "/patients/" id),
                              :format (ajax/json-request-format),
                              :response-format (ajax/json-response-format
                                                 {:keywords? true}),
                              :on-success [:delete-patient-success],
                              :on-failure [:failure]}}))

(reg-event-db
  :delete-patient-success
  (fn [db [_ _]]
    {:db (assoc db
           :flash-message {:flash-type "success",
                           :flash-message "Patient was deleted successfully"}),
     :set-hash {:hash "/patients"}}))
