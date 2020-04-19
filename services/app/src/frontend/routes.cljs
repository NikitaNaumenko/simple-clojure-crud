(ns frontend.routes
  (:require [secretary.core :as secretary :refer-macros [defroute]]
            [re-frame.core :as rf :refer [dispatch dispatch-sync]]))

(defroute "/patients" [] (dispatch [:get-patients]))
