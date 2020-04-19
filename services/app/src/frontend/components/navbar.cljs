(ns frontend.components.navbar
  (:require ["react-bootstrap" :as rb]))

(defn Navbar []
  [:> rb/Navbar {:bg "dark" :variant "dark"}
    [:> rb/Nav {:class "mr-auto"}
      [:> rb/Nav.Link {:href "#patients" } "Patients"]
      [:> rb/Nav.Link {:href "#new-patient" } "New Patient"]]])
