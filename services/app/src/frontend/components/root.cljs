(ns frontend.components.root
  (:require [frontend.components.navbar :as nav]))


(defn root []
  [:div#root
   [nav/Navbar]])

