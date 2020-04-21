(ns frontend.routes
  (:require [secretary.core :as secretary :refer-macros [defroute]]
            [re-frame.core :as rf :refer [dispatch]]))


(defroute "/" [] (dispatch [:set-active-page {:page :home}]))
(defroute "/patients" [] (dispatch [:set-active-page {:page :patients}]))
