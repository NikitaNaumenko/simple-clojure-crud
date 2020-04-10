(ns hs-test.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hs-test.models.patient :as patient]
            [hs-test.views :as views]
            [hs-test.utils :as utils]
            [ring.util.response :refer :all]))

(defn index []
  (let [patients (patient/get-patients)]
    (println patients)
    (views/index patients)))

(defn show [id]
  (let [patient (patient/get-patient (utils/parse-int id))]
    (views/show patient)))

(defn edit [id]
  (let [patient (patient/get-patient (utils/parse-int id))]
    (views/edit patient)))

(defn create [params]
  (redirect "/"))

(defn update [params]
  (redirect "/"))

(defroutes app
  (GET "/patients" [] (index))
  (GET "/patients/:id" [id] (show id))
  (GET "/patients/:id/edit" [id] (edit id))
  (POST "/patients" [params] (create params))
  (PATCH "/patients/:id" [params] (update params))
  (route/not-found "<h1>Page not found</h1>"))
