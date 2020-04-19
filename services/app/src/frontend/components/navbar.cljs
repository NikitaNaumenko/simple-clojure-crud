(ns frontend.components.navbar
  (:require ["react-bootstrap" :as rb]
            [accountant.core :as accountant]))

(defn Navbar []
  [:> rb/Navbar {:bg "dark" :variant "dark"}
    [:> rb/Nav {:class "mr-auto"}
      [:> rb/Nav.Link {:on-click #(accountant/navigate! "/patients") } "Patients"]
      [:> rb/Nav.Link {:href "#new-patient" } "New Patient"]]])
