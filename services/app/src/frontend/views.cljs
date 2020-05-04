(ns frontend.views
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            ["react-bootstrap" :as rb]))

(defn home []
  [:h1 "hello"])

(defn patients []
  (let [
        default {:query ""}
        content (reagent/atom default) ]
    (fn []
      [:div.container
        [:div.row
          [:div.col-12
            [:div.p4
              (let [{:keys [query]} @content
                   search-patients (fn [event form]
                                     (.preventDefault event)
                                     (rf/dispatch [:get-patients form]))]
              [:> rb/Form {:on-submit #(search-patients % @content)}
               [:> rb/Form.Group {:controlId "formGroupFullName"}
                [:> rb/Form.Label "Search"]
                [:> rb/Form.Control {:type "text"
                                     :placeholder "Enter Search query"
                                     :value query
                                     :on-change #(swap! content assoc :query (-> % .-target .-value))
                                     }]]
               [:> rb/Button {:variant "primary" :type "submit"} "Submit"]])]
           (let [patients @(rf/subscribe [:patients])
            columns ["Id" "Full Name" "Date of Birth" "Gender" "Address" "Health Insurance Number" ""] ]
            [:> rb/Table
            [:thead
              [:tr
               (for [column columns]
                ^{:key column} [:th column])]]
            [:tbody
             (for [ patient patients]
               ^{:key (patient :id)}
               [:tr
                [:td (patient :id)]
                [:td (patient :full_name)]
                [:td (patient :date_of_birth)]
                [:td (patient :gender)]
                [:td (patient :address)]
                [:td (patient :health_insurance_number)]
                [:td [:a {:href (str "#/patients/" (patient :id)) :class "btn btn-primary"} "Show"]
                     [:a {:href (str "#/patients/" (patient :id) "/edit") :class "btn btn-primary"} "Edit"]
                     [:a {:href "#/patients"
                          :on-click #(rf/dispatch [:delete-patient (patient :id)])
                          :class "btn btn-danger"} "Delete"]]])]])]]])))

(defn new-patient []
  (let  [default {:full_name ""
                  :date_of_birth ""
                  :address ""
                  :health_insurance_number ""
                  :gender ""}
         form (reagent/atom default)]
    (fn []
      (let [{:keys [full_name date_of_birth address health_insurance_number gender]} @form
        create-patient (fn [event form]
                         (.preventDefault event)
                         (rf/dispatch [:create-patient form]))]
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
           [:> rb/Form.Group {:controlId "formGroupGender"}
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

(defn edit-patient []
  (let [{full_name :full_name
         id :id
         date_of_birth :date_of_birth
         address :address
         gender :gender
         health_insurance_number :health_insurance_number } @(rf/subscribe [:edited-patient])
         default {:full_name full_name
                  :date_of_birth date_of_birth
                  :address address
                  :gender gender
                  :health_insurance_number health_insurance_number}
        
         form (reagent/atom default)
         ]
    (let [loaded @(rf/subscribe [:loaded-patient])
          update-patient (fn [event form]
                            (.preventDefault event)
                            (rf/dispatch [:update-patient form id])) ]
          (when loaded
            (fn []
              [:div.container
                [:div.text-center.p-2
                  [:h1 "Edit Patient"]]
                [:div.row.justify-content-center
                  [:div.col-8
                    [:> rb/Form  {:on-submit #(update-patient % @form)}
                      [:> rb/Form.Group {:controlId "formGroupFullName"}
                        [:> rb/Form.Label "Full Name"]
                        [:> rb/Form.Control {:type "text"
                                             :placeholder "Enter Full Name"
                                             :default-value full_name
                                             :on-change #(swap! form assoc :full_name (-> % .-target .-value))}]]
                        [:> rb/Form.Group {:controlId "formGroupDateOfBirth"}
                          [:> rb/Form.Label "Date of Birth"]
                          [:> rb/Form.Control {:type "date"
                                               :default-value date_of_birth
                                               :on-change #(swap! form assoc :date_of_birth (-> % .-target .-value))}]]
                        [:> rb/Form.Group {:controlId "formGroupGender"}
                          [:> rb/Form.Control {:as "select"
                                               :custom "custom"
                                               :default-value gender
                                               :on-change #(swap! form assoc :gender (-> % .-target .-value))}
                            [:option "male"]
                            [:option "female"]]]
                        [:> rb/Form.Group {:controlId "formGroupAddress"}
                          [:> rb/Form.Label "Address"]
                          [:> rb/Form.Control {:type "text" 
                                               :default-value address
                                               :on-change #(swap! form assoc :address (-> % .-target .-value))}]]
                        [:> rb/Form.Group {:controlId "formGroupHealthInsuranceNumber"}
                          [:> rb/Form.Label "Health Insurance Number"]
                          [:> rb/Form.Control {:type "text"
                                               :default-value health_insurance_number
                                               :on-change #(swap! form assoc :health_insurance_number (-> % .-target .-value))}]]
                        [:> rb/Button {:variant "primary" :type "submit"} "Submit"]]]]])))))

(defn show-patient []
  (let [{full_name :full_name
         id :id
         date_of_birth :date_of_birth
         address :address
         gender :gender
         health_insurance_number :health_insurance_number } @(rf/subscribe [:show-patient])]
[:div.container
                [:div.text-center.p-2
                  [:h1 "Show Patient"]]
                [:div.row.justify-content-center
                  [:div.col-8
                    [:div full_name]
                    [:div date_of_birth]
                    [:div gender]
                    [:div address]
                    [:div health_insurance_number]]]]))

(defn pages [page-name]
  (case page-name
    :home [home]
    :patients [patients]
    :new-patient [new-patient]
    :edit-patient [edit-patient]
    :show-patient [show-patient]
    [home]))

(defn root []
  (let [active-page @(rf/subscribe [:active-page])
        {:keys [flash-type flash-message]} @(rf/subscribe [:flash-message])]
    [:div#root
      [:> rb/Navbar {:bg "dark" :variant "dark"}
        [:> rb/Nav {:class "mr-auto"}
          [:> rb/Nav.Link {:href "#/" } "Home"]
          [:> rb/Nav.Link {:href "#/patients"} "Patients"]
          [:> rb/Nav.Link {:href "#/patients/new"} "New Patient"]]]

      (when flash-message
        [:> rb/Alert {:key flash-type :variant flash-type} flash-message])
      [pages active-page]]))
