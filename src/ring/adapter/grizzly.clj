(ns ring.adapter.grizzly
  (:require
   [ring.util.servlet :refer [build-request-map update-servlet-response]])
  (:import
   (org.glassfish.grizzly.http.server HttpServer NetworkListener)
   (org.glassfish.grizzly.servlet WebappContext)
   (javax.servlet.http HttpServlet)
   (java.lang Thread)))

(defn- create-servlet
  "Create a proxied HttpServlet implementation for a given Ring handler."
  [handler]
  (proxy [HttpServlet] []
    (service [request response]
      "Override the `service` method in HttpServlet."
      (let [request-map (build-request-map request) response-map (handler request-map)]
        (when response-map
          (update-servlet-response response response-map))))))

(defn- create-server
  "Create a Grizzly HttpServer instance."
  [options]
  (let [server (HttpServer.)]
    (.addListener server (NetworkListener. "grizzly" (options :host "0.0.0.0") (options :port 80)))
    server))

(defn- deploy-server
  "Deploy a Grizzly HttpServer instance with a given HttpServlet instance."
  [server servlet]
  (let [ctx (WebappContext. "grizzly" "/")]
    (.addServlet ctx "grizzly" servlet)
    (.deploy ctx server)))

(defn run-grizzly
  "Start a Grizzly HTTP server to serve the given handler according to the supplied options:"
  [handler options]
  (let [server (create-server (dissoc options :configurator))]
    (deploy-server server (create-servlet handler))
    (.start server)
    (when (:join? options true)
      (.join (Thread/currentThread)))
    server))
