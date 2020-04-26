(ns frontend.core
  (:require [shadow.dom :as dom]
            [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [frontend.events]
            [goog.events :as events]
            [frontend.views :as views]
            [secretary.core :as secretary :refer-macros [defroute]]
            [frontend.subs :as subs]
            [accountant.core :as accountant])
  (:import [goog History]
           [goog.history EventType]))

(defn render []
  (rdom/render [views/root] (dom/by-id "app")))

(defn reload! []
  (rf/clear-subscription-cache!)
  (render))
(defn routes
  []
  (set! (.-hash js/location) "/")      ;; on app startup set location to "/"
  (secretary/set-config! :prefix "#")  ;; and don't forget about "#" prefix
  (defroute "/" [] (rf/dispatch [:set-active-page {:page :home}]))
  (defroute "/patients" [] (rf/dispatch [:set-active-page {:page :patients}]))
  (defroute "/patients/:id" [id] (rf/dispatch [:set-active-page {:page :show-patient :id id}]))
  (defroute "/patients/:id/edit" [id] (rf/dispatch [:set-active-page {:page :edit-patient :id id}]))
  (defroute "/patients/new" [] (rf/dispatch [:set-active-page {:page :new-patient}])))
; 
(def history
  (doto (History.)
    (events/listen EventType.NAVIGATE
                   (fn [event] (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn main! []
  (rf/dispatch-sync [:initialize-db])
  (routes)
  (reload!))

(main!)
