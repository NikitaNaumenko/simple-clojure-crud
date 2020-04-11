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
  (patient/create-patient (:params params))
  (redirect "/patients"))

(defn update-p [params]
  (patient/update-patient (:params params))
  (redirect "/patients"))

(defroutes c-routes
  (GET "/patients" [] (index))
  (GET "/patients/:id" [id] (show id))
  (GET "/patients/new" [] (new-p))
  (POST "/patients" params (create params))
  (GET "/patients/:id/edit" [id] (edit id))
  (PATCH "/patients/:id" params (update-p params))
  (route/not-found "<h1>Page not found</h1>"))

(def app 
  (-> c-routes
      (wrap-params)))

