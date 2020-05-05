(ns backend.utils
  (:require [clojure.string :refer [blank?]]))

(defn parse-int [string]
  (Integer. (re-find #"[0-9]*" string)))

(defn parse-date [date]
  (when (not (blank? date))
    (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd")
            date)))

(defn date-to-str [date]
  (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") date))
