(ns backend.handler
  (:require [backend.models.patient :as db]
            [backend.views :as views]
            [compojure.core :as compojure]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]))

(defn index []
  (let [patients (db/get-patients)]
    {:patients patients}))

(compojure/defroutes routes
  (compojure/GET "/" [] (views/layout))
  (compojure/GET "/patients" [] (response (index))))

(def app 
  (-> #'routes
      (wrap-resource "public")
      (wrap-json-response)))

(defonce server (jetty/run-jetty app {:port 3000  :join? false}))
(comment
  (.start server)
  (.stop server))


