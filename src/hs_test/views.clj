(ns hs-test.views
  (:require [hiccup.core :as hc]))

(defn layout [title & content]
  (hc/html
    [:html
     [:head
      [:title title]]
     [:body
      [:div.container content]]]))

(defn index [patients]
  (layout "Index"
          [:table
           [:thead
            [:tr
              [:th "Id"]
              [:th "Full name"]
              [:th "Date of birth"]
              [:th "Sex"]
              [:th "Address"]
              [:th "Health Insurance number"]]]
           [:tbody
            (for [patient patients]
              [:tr
               [:td (patient :patients/id)]
               [:td (patient :patients/full_name)]
               [:td (patient :patients/date_of_birth)]
               [:td (patient :patients/sex)]
               [:td (patient :patients/address)]
               [:td (patient :patients/health_insurance_number)]])]]))

(defn show [patient]
  (layout "Show"
          [:div
           (patient :patients/id)]))

(defn edit [patient]
  (layout "Edit"
          [:div
           (patient :patients/id)]))
