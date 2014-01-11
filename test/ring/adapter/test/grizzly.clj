(ns ring.adapter.test.grizzly
  (:use
   clojure.test)
  (:require
   [clj-http.client :as http]
   [ring.adapter.grizzly :refer [run-grizzly]]))

(defn- hello-world [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello, World!"})

(defmacro with-server [app options & body]
  `(let [server# (run-grizzly ~app ~(assoc options :join? false))]
     (try
       ~@body
       (finally (.stop server#)))))

(deftest test-run-grizzly
  (testing "HTTP server"
    (with-server hello-world {:port 7337}
      (let [response (http/get "http://localhost:7337")]
        (is (= (:status response) 200))
        (is (.startsWith (get-in response [:headers "content-type"]) "text/plain"))
        (is (= (:body response) "Hello, World!"))))))
