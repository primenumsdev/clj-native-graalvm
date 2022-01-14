(ns web-app.web
  (:require [ring.adapter.jetty9 :refer [run-jetty]]
            [ring.middleware.cors :refer [wrap-cors]]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as rrc]
            [reitit.coercion.spec :as rcs]
            [reitit.ring.middleware.parameters :as rrmp]
            [reitit.ring.middleware.muuntaja :as rrmm]
            [reitit.swagger :as rs]
            [reitit.swagger-ui :as rsu]
            [muuntaja.core :as m]
            [jsonista.core :refer [read-value write-value-as-string object-mapper]]))

(def json-kw-key-mapper
  (object-mapper {:decode-key-fn true}))

(defn json-read [json-str]
  (read-value json-str json-kw-key-mapper))

(defn json-write [obj]
  (write-value-as-string obj))

(def app
  (ring/routes
    (rsu/create-swagger-ui-handler {:path "/"})
    (ring/ring-handler
      (ring/router
        [["/swagger.json" {:get {:no-doc  true
                                 :swagger {:basePath            "/"
                                           :info                {:title       "Web App"
                                                                 :description "Endpoints"
                                                                 :version     "0.1.0"}
                                           :securityDefinitions {:apiAuth {:type        "apiKey"
                                                                           :name        "Authorization"
                                                                           :in          "header"
                                                                           :description "Enter bearer token in format **Bearer &lt;token>"}}
                                           }
                                 :handler (rs/create-swagger-handler)}}]
         ["/hello" {
                    :swagger {:security [{:apiAuth []}]}
                    ;;:middleware [auth]
                    :get     {:summary    "GET hello"
                              :parameters {:query {:name string?}}
                              :responses  {200 {}}
                              :handler    (fn [req res raise]
                                            (let [name (-> req :parameters :query :name)]
                                              (res {:status 200 :body (json-write {:msg (str "Welcome " name)})})))
                              }
                    :post    {:summary    "POST hello"
                              :parameters {:body {:name string?}}
                              :responses  {200 {}}
                              :handler    (fn [req res raise]
                                            (let [name (-> req :parameters :body :name)]
                                              (res {:status 200 :body (json-write {:msg (str "Welcome " name)})})))
                              }
                    }]]
        {:data {:muuntaja   m/instance
                :coercion   rcs/coercion
                :middleware [rrmm/format-middleware
                             rrmp/parameters-middleware
                             rrc/coerce-exceptions-middleware
                             rrc/coerce-response-middleware
                             rrc/coerce-request-middleware
                             [wrap-cors
                              :access-control-allow-origin [#".*"]
                              :access-control-allow-methods [:get :put :post :patch :delete]]]}}
        )
      (ring/create-default-handler
        {:not-found (constantly {:status 404 :body "URL not found"})}))
    ))

(defn start-server [{:keys [port]}]
  (run-jetty #'app
             {:port   port
              :async? true
              :join?  false}))
