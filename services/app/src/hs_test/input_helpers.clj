(ns hs-test.input_helpers
  (:require [hiccup.def :refer [defelem]]))

(defn- input-field
  "Creates a new <input> element."
  [type name value]
  [:input {:type  type
           :name  name
           :id    name
           :value value}])
 
(defelem date-field
  ([name] (date-field name nil))
  ([name value] (input-field "date" name value)))
