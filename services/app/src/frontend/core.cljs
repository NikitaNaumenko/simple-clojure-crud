(ns frontend.core
  (:require [shadow.dom :as dom]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [reagent.core :as rc]
            [frontend.subs :as subs]
            [frontend.events]
            [frontend.debounce]
            [goog.events :as events]
            [frontend.views :as views]
            [secretary.core :as secretary :refer-macros [defroute]])
  (:import [goog History]
           [goog.history EventType]))

(defn render [] (rdom/render [views/root] (dom/by-id "app")))

(defn reload! [] (rf/clear-subscription-cache!) (render))

(secretary/set-config! :prefix "#")
(defroute "/" [] (rf/dispatch [:set-active-page {:page :home}]))
(defroute "/patients/new"
          []
          (rf/dispatch [:set-active-page {:page :new-patient}]))
(defroute "/patients"
          [query-params]
          (rf/dispatch [:set-active-page
                        {:page :patients, :query query-params}]))

(defroute "/patients/:id"
          [id]
          (rf/dispatch [:set-active-page {:page :show-patient, :id id}]))
(defroute "/patients/:id/edit"
          [id]
          (rf/dispatch [:set-active-page {:page :edit-patient, :id id}]))

(def history
  (doto (History.)
    (events/listen EventType.NAVIGATE
                   (fn [event] (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn ^:export main [] (reload!))
