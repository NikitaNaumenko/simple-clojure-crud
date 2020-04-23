(ns frontend.views
  (:require [re-frame.core :as rf]
            [frontend.components.table :as table]
            [frontend.components.navbar :as nav]
            [reagent.core :as reagent]
            ["react-bootstrap" :as rb]))

(defn home []
  [table/Table [:jopa]]
  )

(defn patients []
  (let [patients @(rf/subscribe [:patients])]
    [table/Table ["Id" "Full Name" "Date of Birth" "Gender" "Address" "Health Insurance Number"]
                 patients]))

(defn new-patients []
  (let  [default {:full_name ""}
        form (reagent/atom default)]
     (let [{:keys [full_name]} @form
        create-patient (fn [event form]
                         (.preventDefault event)
                         (rf/dispatch [:create-patient form])) ] 
    [:div.container
       [:div.text-center.p-2
        [:h1 "New Patient"]]
       [:div.row.justify-content-center
        [:div.col-8
         [:> rb/Form {:on-submit #(create-patient % @form)}
           [:> rb/Form.Group {:controlId "formGroupFullName"}
            [:> rb/Form.Label "Full Name"]
            [:> rb/Form.Control {:type "text" :placeholder "Enter Full Name"}]]
           [:> rb/Form.Group {:controlId "formGroupFullName"}
            [:> rb/Form.Label "Date of Birth"]
            [:> rb/Form.Control {:type "date"}]]
           [:> rb/Form.Group {:controlId "formGroupFullName"}
            [:> rb/Form.Check {:type "radio" :id "male" :label "Male"}]
            [:> rb/Form.Check {:type "radio" :id "female" :label "Female"}]]
           [:> rb/Form.Group {:controlId "formGroupAddress"}
            [:> rb/Form.Label "Address"]
            [:> rb/Form.Control {:type "text"}]]
           [:> rb/Form.Group {:controlId "formGroupHealthInsuranceNumber"}
            [:> rb/Form.Label "Health Insurance Number"]
            [:> rb/Form.Control {:type "text"}]]
           [:> rb/Button {:variant "primary" :type "submit"} "Submit"]
           ]]]])))

(defn pages [page-name]
  (case page-name
    :home [home]
    :patients [patients]
    :new-patients [new-patients]
    [home]))

(defn root []
  (let [active-page @(rf/subscribe [:active-page])]
    [:div#root
      [nav/Navbar]
      [pages active-page]]))
