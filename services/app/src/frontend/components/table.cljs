(ns frontend.components.table
  (:require ["react-bootstrap" :as rb]))

(defn Table [columns records]
  [:> rb/Table
    [:thead
      [:tr
       (for [column columns]
        [:th column])]]
     [:tbody
     (for [record records]
       ^{:key record}[:tr
        [:td (record :id)]
        ])]     
    ])


