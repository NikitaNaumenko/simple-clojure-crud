(ns frontend.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]))

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
    (println result)
    (into db {::user    (:user result)
              ::loading false})))


(reg-event-db ::failure
  (fn [db [_ _result]]
    (into db {::user    nil
              ::loading false})))
