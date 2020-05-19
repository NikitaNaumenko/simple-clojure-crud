(ns backend.core
  (:gen-class)
  (:require [backend.handler :refer [app]]
            [ring.adapter.jetty :as jetty]))

(defn -main [& _] (jetty/run-jetty app {:port 3000, :join? false}))
