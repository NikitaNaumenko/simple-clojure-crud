(ns backend.handler
  (:require [backend.models.patient :as db]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [response]]
            [backend.utils :as utils]
            [clojure.core.match :refer [match]]
            [backend.db :as db-connection]
            [clojure.string :as str]))

(defn index-patients
  [db-connection {params :params}]
  (let [patients (db/get-patients db-connection params)]
    {:status 200, :body {:patients patients}}))

(defn create-patient
  [db-connection {params :params}]
  (let [patient-params (get-in params ["patient"])
        patient (db/create-patient db-connection patient-params)]
    {:status 200, :body {:patient patient}}))

(defn show-patient
  [db-connection id _]
  (let [patient (db/get-patient db-connection (utils/parse-int id))]
    (if patient {:status 200, :body {:patient patient}} {:status 404})))

(defn edit-patient
  [db-connection id _]
  (let [{date_of_birth :date_of_birth, :as patient}
          (db/get-patient db-connection (utils/parse-int id))
        body {:patient (merge patient
                              {:date_of_birth (utils/date-to-str
                                                date_of_birth)})}]
    {:status 200, :body body}))

(defn update-patient
  [db-connection id {params :params}]
  (let [patient
          (db/update-patient db-connection id (get-in params ["patient"]))]
    {:status 200, :body {:patient patient}}))

(defn destroy-patient
  [db-connection id request]
  (db/destroy-patient db-connection (utils/parse-int id)))

(def root-path "/")
(defn root-path? [path] (= path root-path))

(defn find-action
  [request-method id action]
  (match [request-method id action]
    [:get nil nil] :index
    [:get id nil] :show
    [:get id "edit"] :edit
    [:post nil nil] :create
    [:patch id nil] :update
    [:delete id nil] :destroy))

(defn build-route-meta
  [path]
  (zipmap [:route :id :action] (filter #(not= "" %) (str/split path #"\/"))))

(defn build-route
  [method path]
  (if (root-path? path)
    {:method method, :route path}
    (let [{action :action, id :id, route :route} (build-route-meta path)]
      (assoc {:method method, :route route, :id id}
        :action (find-action method id action)))))

(defn dispatch
  [{uri :uri, request-method :request-method, :as request} db-connection]
  (let [{id :id, :as route} (build-route request-method uri)]
    (match [route]
      [{:method :get, :route "/"}] (index-patients db-connection request)
      [{:method :get, :route "patients", :action :edit}]
        (edit-patient db-connection id request)
      [{:method :get, :route "patients", :action :show}]
        (show-patient db-connection id request)
      [{:method :post, :route "patients", :action :create}]
        (create-patient db-connection request)
      [{:method :patch, :route "patients", :action :update}]
        (update-patient db-connection id request)
      [{:method :delete, :route "patients", :action :destroy}]
        (destroy-patient db-connection id request)
      :else {:status 404, :body "Not found"})))

(defn app
  [db-connection]
  (-> (fn [request] (dispatch request db-connection))
      (wrap-resource "public")
      (wrap-json-params)
      (wrap-json-response)
      (wrap-params)))

(comment (defonce server
                  (jetty/run-jetty (app (db-connection/ds-dev))
                                   {:port 3000, :join? false}))
         (.start server)
         (.stop server))
