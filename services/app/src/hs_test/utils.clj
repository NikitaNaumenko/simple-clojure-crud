(ns hs-test.utils)

(defn parse-int [string]
  (Integer. (re-find #"[0-9]*" string)))

(defn parse-date [date]
  (.parse
        (java.text.SimpleDateFormat. "yyyy-MM-dd")
        date))
