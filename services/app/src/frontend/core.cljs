(ns frontend.core
  (:require [shadow.dom :as dom]
            [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [frontend.events]
            [frontend.routes]
            [frontend.views :as views]
            [frontend.subs :as subs]
            [accountant.core :as accountant]
            [secretary.core :as secretary]))

(defn render []
  (rdom/render [views/root] (dom/by-id "app")))

(defn reload! []
  (rf/clear-subscription-cache!)
  (render))


(defn main! []
  (rf/dispatch-sync [:initialize-db])
  (accountant/configure-navigation!
    {:nav-handler
      (fn [path]
        (secretary/dispatch! path))
      :path-exists?
      (fn [path]
        (secretary/locate-route path))})
  (reload!))

(main!)
