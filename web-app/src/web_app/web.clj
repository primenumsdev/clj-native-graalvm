(ns web-app.web
  (:require [ring.adapter.jetty9 :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as rrc]))

(def app
  (ring/ring-handler
    (ring/router
      ["/hello" {:get (fn [req]
                        {:status 200 :body "OK"})}])
    (ring/create-default-handler
      {:not-found (constantly {:status 404 :body "URL not found"})})))

(defn start-server [port]
  (run-jetty #'app
             {:port  port
              :join? false}))
