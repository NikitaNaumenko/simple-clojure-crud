(ns backend.views
  (:require [hiccup.page :as hp]))

(defn layout []
  (hp/html5 [:head
             (hp/include-css "/css/bootstrap.min.css")
             ]
    [:body
     [:main#app]
      (hp/include-js "/js/main.js") ]))

