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
  (let  [default {:full_name ""
                  :date_of_birth ""
                  :address ""
                  :health_insurance_number ""
                  :gender ""}
         form (reagent/atom default)]
    (fn []
      (let [{:keys [full_name date_of_birth]} @form
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
            [:> rb/Form.Control {:type "text"
                                 :placeholder "Enter Full Name"
                                 :value full_name
                                 :on-change #(swap! form assoc :full_name (-> % .-target .-value))
                                 }]]
           [:> rb/Form.Group {:controlId "formGroupDateOfBirth"}
            [:> rb/Form.Label "Date of Birth"]
            [:> rb/Form.Control {:type "date"
                                 :value date_of_birth
                                 :on-change #(swap! form assoc :date_of_birth (-> % .-target .-value)) }]]
           [:> rb/Form.Group {:controlId "formGroupFullName"}
            [:> rb/Form.Control {:as "select"
                                 :custom "custom"
                                 :value gender
                                 :on-change #(swap! form assoc :gender (-> % .-target .-value))
                                 }
             [:option "male"]
             [:option "female"]
             ]]
           [:> rb/Form.Group {:controlId "formGroupAddress"}
            [:> rb/Form.Label "Address"]
            [:> rb/Form.Control {:type "text" 
                                 :value address
                                 :on-change #(swap! form assoc :address (-> % .-target .-value))
                                 }]]
           [:> rb/Form.Group {:controlId "formGroupHealthInsuranceNumber"}
            [:> rb/Form.Label "Health Insurance Number"]
            [:> rb/Form.Control {:type "text"
                                 :value health_insurance_number
                                 :on-change #(swap! form assoc :health_insurance_number (-> % .-target .-value))
                                 }]]
           [:> rb/Button {:variant "primary" :type "submit"} "Submit"]]]]]))))

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
