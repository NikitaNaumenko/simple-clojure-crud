(ns frontend.components.table
  (:require ["react-bootstrap" :as rb]))

(defn Table [columns]
  [:> rb/Table
    [:thead
      [:tr
       (for [column columns]
        [:th column])]]])
