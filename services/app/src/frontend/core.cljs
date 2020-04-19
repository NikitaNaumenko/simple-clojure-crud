(ns frontend.core
  (:require [shadow.dom :as dom]
            [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [frontend.components.root :refer [root]]))

;; initialise re-frame by broadcasting event
(rf/dispatch-sync [:initialize])

(defn render []
  (rdom/render [root] (dom/by-id "app")))


(defn reload! []
  (println "(reload!)")
  (render))


(defn main! []
  (println "(main!)")
  (reload!))

(main!)
