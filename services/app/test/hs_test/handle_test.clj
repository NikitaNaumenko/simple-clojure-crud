(ns hs-test.handle-test 
   (:require [clojure.test :refer :all]
             [hs-test.handler :refer [app]]))

(defn test-request [resource web-app & params]
  (web-app {:request-method :get :uri resource :params params}))

(deftest index-test
  (is (= 200
           (:status (test-request "/patients" app)))))
