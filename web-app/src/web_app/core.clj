(ns web-app.core
  (:require [web-app.web :as w])
  (:gen-class))

(set! *warn-on-reflection* true)

(defn -main
  [& args]
  (println "Starting web server...")
  (def server (w/start-server 3000)))

(comment
  (-main)
  )
