(ns frontend.views
  (:require [re-frame.core :as rf]
    [frontend.components.table :as table]
            [frontend.components.navbar :as nav]))

(defn home []
  [table/Table [:jopa]]
  )

(defn patients []
  [table/Table [:id :first-name :last-name]])


(defn pages [page-name]
  (println page-name)
  (case page-name
    :home [home]
    :patients [patients]
    [home]))

(defn root []
  (let [active-page @(rf/subscribe [:active-page])]
    [:div#root
      [nav/Navbar]
      [pages active-page]]))
