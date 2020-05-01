(ns frontend.db)

(def default-db
  {:patients []
   :active-page :home
   :edited-patient {:full_name "" :gender "" :address "" :health_insurance_number "" :date_of_birth ""}
   
   :loaded-patient false
   }
  )

