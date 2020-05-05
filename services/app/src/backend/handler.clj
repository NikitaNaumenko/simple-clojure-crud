(ns backend.handler
  (:require
   [backend.models.patient :as db]
   [backend.views :as views]
   [compojure.core :as compojure]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.resource :refer [wrap-resource]]
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
  (let [{:keys [date_of_birth] :as patient}
        (db/get-patient (utils/parse-int id))]
    {:patient (merge patient {:date_of_birth (utils/date-to-str date_of_birth)})}))

(defn update-patient [id params]
  (db/update-patient id params))

(defn destroy-patient [id]
  (db/destroy-patient (utils/parse-int id)))

(compojure/defroutes routes
  (compojure/GET "/" [] (views/layout))
  (compojure/GET "/patients" params (response (index-patients (params :query-params))))
  (compojure/GET "/patients/:id" [id] (response (show-patient id)))
  (compojure/POST "/patients" params (response (create-patient params)))
  (compojure/GET "/patients/:id/edit" [id] (response (edit-patient id)))
  (compojure/PATCH "/patients/:id" [id patient] (response (update-patient id patient)))
  (compojure/DELETE "/patients/:id" [id] (response (destroy-patient id))))

(def app
  (-> #'routes
      (wrap-resource "public")
      (wrap-json-params)
      (wrap-json-response)
      (wrap-params)))

(comment
  (defonce server (jetty/run-jetty app {:port 3000  :join? false}))
  (.start server)
  (.stop server))
