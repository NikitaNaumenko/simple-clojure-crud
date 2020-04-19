(ns frontend.events
  (:require [re-frame.core :refer [reg-event-db]]))

(reg-event-db
  :get-patients
  (fn [db [_ patients]]
    (assoc db :patients patients)))

