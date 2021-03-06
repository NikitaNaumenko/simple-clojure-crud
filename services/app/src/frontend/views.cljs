(ns frontend.views
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            ["react-bootstrap" :as rb]))

(defn home [] [:h1 "hello"])

(defn search
  []
  (let [sub (rf/subscribe [:filter])
        on-search #(rf/dispatch [:filter-table (.. % -target -value)])]
    (fn []
      (let [m @sub]
        [:input.search
         {:placeholder "Search", :on-change on-search, :value m}]))))

(defn patients
  []
  (fn [] [:div.container [:div [search]]
          [:div.row
           [:div.col-12
            (let [patients @(rf/subscribe [:patients])
                  columns ["Id" "Full Name" "Date of Birth" "Gender" "Address"
                           "Health Insurance Number" ""]]
              [:> rb/Table
               [:thead [:tr (for [column columns] ^{:key column} [:th column])]]
               [:tbody
                (for [patient patients]
                  ^{:key (patient :id)}
                  [:tr [:td (patient :id)] [:td (patient :full_name)]
                   [:td (patient :date_of_birth)] [:td (patient :gender)]
                   [:td (patient :address)]
                   [:td (patient :health_insurance_number)]
                   [:td
                    [:a
                     {:href (str "#/patients/" (patient :id)),
                      :class "btn btn-primary"} "Show"]
                    [:a
                     {:href (str "#/patients/" (patient :id) "/edit"),
                      :class "btn btn-primary"} "Edit"]
                    [:a
                     {:href "#/patients",
                      :on-click #(rf/dispatch [:delete-patient (patient :id)]),
                      :class "btn btn-danger"} "Delete"]]])]])]]]))

(defn exists-error-partial
  []
  [:> rb/Alert {:key "error", :variant "danger"}
   "Health insurance number already exists"])

(defn errors-partial
  [error]
  [:> rb/Alert {:key "error", :variant "danger"} error])


(defn new-patient
  []
  (let [form (rf/subscribe [:new-patient])]
    (fn []
      (let [{:keys [full_name date_of_birth address health_insurance_number
                    gender]}
              @form
            exists-error @(rf/subscribe [:health-insurance-number-exists?])
            errors @(rf/subscribe [:errors])
            create-patient (fn [event form]
                             (.preventDefault event)
                             (rf/dispatch [:create-patient form]))]
        [:div.container [:div.text-center.p-2 [:h1 "New Patient"]]
         [:div.row.justify-content-center
          [:div.col-8 (when exists-error (exists-error-partial))
           (when errors (errors-partial errors))
           [:> rb/Form {:on-submit #(create-patient % @form)}
            [:> rb/Form.Group {:controlId "formGroupFullName"}
             [:> rb/Form.Label "Full Name"]
             [:> rb/Form.Control
              {:type "text",
               :placeholder "Enter Full Name",
               :value full_name,
               :on-change #(rf/dispatch [:change-new-patient
                                         (.. % -target -value) :full_name])}]]
            [:> rb/Form.Group {:controlId "formGroupDateOfBirth"}
             [:> rb/Form.Label "Date of Birth"]
             [:> rb/Form.Control
              {:type "text",
               :value date_of_birth,
               :on-change #(rf/dispatch [:change-new-patient
                                         (.. % -target -value)
                                         :date_of_birth])}]]
            [:> rb/Form.Group {:controlId "formGroupGender"}
             [:> rb/Form.Control
              {:as "select",
               :custom "custom",
               :value gender,
               :on-change #(rf/dispatch [:change-new-patient
                                         (.. % -target -value) :gender])}
              [:option "male"] [:option "female"]]]
            [:> rb/Form.Group {:controlId "formGroupAddress"}
             [:> rb/Form.Label "Address"]
             [:> rb/Form.Control
              {:type "text",
               :value address,
               :on-change #(rf/dispatch [:change-new-patient
                                         (.. % -target -value) :address])}]]
            [:> rb/Form.Group {:controlId "formGroupHealthInsuranceNumber"}
             [:> rb/Form.Label "Health Insurance Number"]
             [:> rb/Form.Control
              {:type "text",
               :value health_insurance_number,
               :on-change #(rf/dispatch [:change-patient-insurance-number
                                         (.. % -target -value) :new-patient])}]]
            [:> rb/Button {:variant "primary", :type "submit"} "Submit"]]]]]))))

(defn edit-patient
  []
  (let [loaded @(rf/subscribe [:loaded-patient])
        current_health_insurance_number @(rf/subscribe
                                           [:current_health_insurance_number])]
    (when loaded
      (fn []
        (let [form (rf/subscribe [:edited-patient])
              {id :id,
               full_name :full_name,
               gender :gender,
               health_insurance_number :health_insurance_number,
               date_of_birth :date_of_birth,
               address :address}
                @form
              exists-error @(rf/subscribe [:health-insurance-number-exists?])
              update-patient (fn [event form]
                               (.preventDefault event)
                               (rf/dispatch [:update-patient form id]))]
          [:div.container [:div.text-center.p-2 [:h1 "Edit Patient"]]
           (when exists-error (exists-error-partial))
           [:div.row.justify-content-center
            [:div.col-8
             [:> rb/Form {:on-submit #(update-patient % @form)}
              [:> rb/Form.Group {:controlId "formGroupFullName"}
               [:> rb/Form.Label "Full Name"]
               [:> rb/Form.Control
                {:type "text",
                 :placeholder "Enter Full Name",
                 :value full_name,
                 :on-change #(rf/dispatch [:change-edited-patient
                                           (.. % -target -value) :full_name])}]]
              [:> rb/Form.Group {:controlId "formGroupDateOfBirth"}
               [:> rb/Form.Label "Date of Birth"]
               [:> rb/Form.Control
                {:type "date",
                 :value date_of_birth,
                 :on-change #(rf/dispatch [:change-edited-patient
                                           (.. % -target -value)
                                           :date_of_birth])}]]
              [:> rb/Form.Group {:controlId "formGroupGender"}
               [:> rb/Form.Control
                {:as "select",
                 :custom "custom",
                 :value gender,
                 :on-change #(rf/dispatch [:change-edited-patient
                                           (.. % -target -value) :gender])}
                [:option "male"] [:option "female"]]]
              [:> rb/Form.Group {:controlId "formGroupAddress"}
               [:> rb/Form.Label "Address"]
               [:> rb/Form.Control
                {:type "text",
                 :value address,
                 :on-change #(rf/dispatch [:change-edited-patient
                                           (.. % -target -value) :address])}]]
              [:> rb/Form.Group {:controlId "formGroupHealthInsuranceNumber"}
               [:> rb/Form.Label "Health Insurance Number"]
               [:> rb/Form.Control
                {:type "text",
                 :value health_insurance_number,
                 :on-change #(rf/dispatch [:change-patient-insurance-number
                                           (.. % -target -value) :edited-patient
                                           current_health_insurance_number])}]]
              [:> rb/Button {:variant "primary", :type "submit"}
               "Submit"]]]]])))))

(defn show-patient
  []
  (let [{full_name :full_name,
         date_of_birth :date_of_birth,
         address :address,
         gender :gender,
         health_insurance_number :health_insurance_number}
          @(rf/subscribe [:show-patient])]
    [:div.container [:div.text-center.p-2 [:h1 "Show Patient"]]
     [:div.row.justify-content-center
      [:div.col-8 [:div full_name] [:div date_of_birth] [:div gender]
       [:div address] [:div health_insurance_number]]]]))

(defn pages
  [page-name]
  (case page-name
    :home [home]
    :patients [patients]
    :new-patient [new-patient]
    :edit-patient [edit-patient]
    :show-patient [show-patient]
    [home]))

(defn root
  []
  (let [active-page @(rf/subscribe [:active-page])
        {:keys [flash-type flash-message]} @(rf/subscribe [:flash-message])]
    [:div#root
     [:> rb/Navbar {:bg "dark", :variant "dark"}
      [:> rb/Nav {:class "mr-auto"} [:> rb/Nav.Link {:href "#/"} "Home"]
       [:> rb/Nav.Link {:href "#/patients"} "Patients"]
       [:> rb/Nav.Link {:href "#/patients/new"} "New Patient"]]]
     (when flash-message
       [:> rb/Alert {:key flash-type, :variant flash-type} flash-message])
     [pages active-page]]))
