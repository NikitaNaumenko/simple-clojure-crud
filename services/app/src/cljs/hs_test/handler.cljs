(ns hs-test.handler
  (:use [jayq.core :only [$ on ajax attr]]))

(enable-console-print!)

(def $body ($ :body))
(def $btn ($ :.destroy-btn))

(on $body "click" :.destroy-btn (fn [e] (js/console.log (attr ($ (.-target e))  "data-link" ) )))
