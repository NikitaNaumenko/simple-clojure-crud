(ns backend.utils)

(defn parse-int [string]
  (Integer. (re-find #"[0-9]*" string)))

(defn parse-date [date]
  (if (not (clojure.string/blank? date))
    (.parse
        (java.text.SimpleDateFormat. "yyyy-MM-dd")
        date)))

