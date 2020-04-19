(ns frontend.core
  (:require [shadow.dom :as dom]
            [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [frontend.events]
            [frontend.routes]
            [accountant.core :as accountant]
            [secretary.core :as secretary]
            [frontend.components.root :refer [root]]))

;; initialise re-frame by broadcasting event
(rf/dispatch-sync [:initialize])

(defn render []
  (rdom/render [root] (dom/by-id "app")))


(defn reload! []
  (println "(sopa!)")
  (render))


(defn main! []
  (accountant/configure-navigation!
    {:nav-handler
      (fn [path]
        (secretary/dispatch! path))
      :path-exists?
      (fn [path]
        (secretary/locate-route path))})
  (reload!))

(main!)
