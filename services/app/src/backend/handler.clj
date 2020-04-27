(ns backend.handler
  (:require
   [backend.models.patient :as db]
   [backend.views :as views]
   [compojure.core :as compojure]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.resource :refer [wrap-resource]]
   [ring.middleware.content-type :refer [wrap-content-type]]
   [ring.middleware.not-modified :refer [wrap-not-modified]]
   [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.util.response :refer [response]]
   [backend.utils :as utils]))

(defn index-patients [params]
  (if (empty? params)
    (let [patients (db/get-patients )]
      {:patients patients})
    (let [patients (db/get-patients params)]
      (println patients)
      {:patients patients})))

(defn create-patient [params]
  (let [patient-params (get-in params [:params "patient"])]
    (db/create-patient patient-params)))

(defn show-patient [id]
  (let [patient (db/get-patient (utils/parse-int id))]
    {:patient patient}))

(defn edit-patient [id]
  (let [patient (db/get-patient (utils/parse-int id))]
    (println patient)
    {:patient patient}))

(defn update-patient [params]
  (let [patient-params (get-in params [:params "patient"])]
    (db/update-patient patient-params)))

(defn destroy-patient [id]
  (db/destroy-patient (utils/parse-int id)))

(compojure/defroutes routes
  (compojure/GET "/" [] (views/layout))
  (compojure/GET "/patients" params (response (index-patients (params :query-params))))
  (compojure/GET "/patients/:id" [id] (show-patient id))
  (compojure/POST "/patients" params (create-patient params))
  (compojure/GET "/patients/:id/edit" [id] (response (edit-patient id)))
  (compojure/PATCH "/patients/:id" params (update-patient params))
  (compojure/DELETE "/patients/:id" [id] (destroy-patient id)))

(def app
  (-> #'routes
      (wrap-resource "public")
      (wrap-json-params)
      (wrap-json-response)
      (wrap-params)))

(defonce server (jetty/run-jetty app {:port 3000  :join? false}))
(comment
  (.start server)
  (.stop server))
