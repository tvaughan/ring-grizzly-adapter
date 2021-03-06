(ns ring.adapter.grizzly
  (:require [ring.util.servlet :refer [servlet]
             :rename {servlet create-servlet}])
  (:import (org.glassfish.grizzly.http.server HttpServer
                                              NetworkListener)
           (org.glassfish.grizzly.servlet WebappContext)))

(def servlet-name "grizzly")

(defn- create-server
  "Create a Grizzly HttpServer instance."
  [options]
  (doto (HttpServer.)
    (.addListener (NetworkListener. servlet-name
                                    (options :host "0.0.0.0")
                                    (options :port 80)))))

(defn- deploy-server
  "Deploy a Grizzly HttpServer instance with a given HttpServlet instance."
  [server servlet]
  (let [ctx (WebappContext. servlet-name "/")]
    (doto (.addServlet ctx servlet-name servlet)
      (.addMapping (into-array ["/*"])))
    (.deploy ctx server))
  server)

(defn run-grizzly
  "Start a Grizzly HTTP server to serve the given handler according to
  the supplied options:"
  [handler options]
  (let [server (create-server (dissoc options :configurator))]
    (.start (deploy-server server (create-servlet handler)))
    (when (:join? options true)
      (.join (Thread/currentThread)))
    server))
