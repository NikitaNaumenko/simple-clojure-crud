(ns hs-test.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hs-test.models.patient :as patient]
            [hs-test.views :as views]
            [hs-test.utils :as utils]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer :all]))

(defn index []
  (let [patients (patient/get-patients)]
    (views/index patients)))

(defn show [id]
  (let [patient (patient/get-patient (utils/parse-int id))]
    (views/show patient)))

(defn edit [id]
  (let [patient (patient/get-patient (utils/parse-int id))]
    (views/edit patient)))

(defn new-p []
  (views/new-p))

(defn create [params]
  (println (:params params))
  (patient/create-patient (:params params))
  (redirect "/"))

; (defn update [params]
;   (redirect "/"))

(defroutes c-routes
  (GET "/patients" [] (index))
  (GET "/patients/new" [] (new-p))
  (GET "/patients/:id" [id] (show id))
  (GET "/patients/:id/edit" [id] (edit id))
  (POST "/patients" params (create params))
  ; (PATCH "/patients/:id" [params] (update params))
  (route/not-found "<h1>Page not found</h1>"))

(def app 
  (-> c-routes
      (wrap-params)))

