(ns backend.utils
  (:require [clojure.string :refer [blank?]]))

(defn parse-int [string] (Integer. (re-find #"[0-9]*" string)))

(defn right-format-date?
  [date]
  (if (re-matches #"([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))" date)
    true
    false))

(defn parse-date
  [date]
  (when (not (blank? date))
    (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd") date)))

(defn date-to-str
  [date]
  (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") date))
