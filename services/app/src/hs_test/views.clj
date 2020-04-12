(ns hs-test.views
  (:require [hiccup.page :as hp]
            [hiccup.element :as he]
            [hiccup.form :as hf]
            [hs-test.input_helpers :as ce]))

(defn layout [title & content]
  (hp/html5 [:head
      [:title title]
      (hp/include-css "https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css")]
    [:body
     [:main.container content]]))

(defn index [patients]
  (layout "Index"
          [:div.p-4
           [:div.d-flex.justify-content-between
             [:h1 "Patients"]
             [:div 
              (he/link-to {:class "btn btn-primary"} "/patients/new" "New Patient")]]]
          [:div
           [:table.table
           [:thead
            [:tr
              [:th "Id"]
              [:th "Full name"]
              [:th "Date of birth"]
              [:th "Gender"]
              [:th "Address"]
              [:th "Health Insurance number"]
              [:th ""]
              ]]
           [:tbody
            (for [patient patients]
              [:tr
               [:td (patient :patients/id)]
               [:td (patient :patients/full_name)]
               [:td (patient :patients/date_of_birth)]
               [:td (patient :patients/gender)]
               [:td (patient :patients/address)]
               [:td (patient :patients/health_insurance_number)]
               [:td (he/link-to {:class "btn btn-secondary"} (str "/patients/" (patient :patients/id) "/edit") "Edit")]
               ])]]]))

(defn show [patient]
  (layout "Show"
          [:div
           (patient :patients/id)]))

; (defn edit [patient]
;   (layout "Edit"
;           [:div
;            (patient :patients/id)]))

(defn new-p []
  (layout "New"
          [:div
           (hf/form-to [:post "/patients"]
                       [:div.form-group
                        (hf/label "full_name" "Full name")
                        (hf/text-field {:class "form-control"} "full_name")
                        ]
                       [:div.form-group
                        (hf/label "date_of_birth" "Date of Birth")
                        (ce/date-field {:class "form-control"} "date_of_birth")
                        ]
                       [:div.form-group
                        (hf/label "gender" "Gender")
                        [:div
                          (hf/label "male" "Male")
                          (hf/radio-button "gender"  false "male")
                          (hf/label "female" "Female")
                          (hf/radio-button "gender"  false "female")]
                        ]
                       [:div.form-group
                        (hf/label "address" "Address")
                        (hf/text-field {:class "form-control"} "address")
                        ]
                       [:div.form-group
                        (hf/label "health_insurance_number" "Health Insurance Number")
                        (hf/text-field {:class "form-control"} "health_insurance_number")
                        ]
                    (hf/submit-button {:class "btn btn-success" :name "submit"} "Save")) ]))

(defn edit [patient]
  (layout "New"
          [:div
           (hf/form-to [:patch (str "/patients/" (patient :patients/id))]
                       [:div.form-group
                        (hf/label "full_name" "Full name")
                        (hf/text-field {:class "form-control"} "full_name" (patient :patients/full_name))
                        ]
                       [:div.form-group
                        (hf/label "date_of_birth" "Date of Birth")
                        (ce/date-field {:class "form-control"} "date_of_birth" (patient :patients/date_of_birth))
                        ]
                       [:div.form-group
                        (hf/label "gender" "Gender")
                        [:div
                          (hf/label "male" "Male")
                          (hf/radio-button "gender"  (= "male" (patient :patients/gender)) "male")
                          (hf/label "female" "Female")
                          (hf/radio-button "gender" (= "female" (patient :patients/gender)) "female")]
                        ]
                       [:div.form-group
                        (hf/label "address" "Address")
                        (hf/text-field {:class "form-control"} "address" (patient :patients/address))
                        ]
                       [:div.form-group
                        (hf/label "health_insurance_number" "Health Insurance Number")
                        (hf/text-field {:class "form-control"} "health_insurance_number" (patient :patients/health_insurance_number))
                        ]
                    (hf/submit-button {:class "btn btn-success" :name "submit"} "Update")) ]))
