(ns cli-app.core
  (:require [clojure.pprint :refer [print-table]]
            [clj-http.lite.client :as http]
            [jsonista.core :as j])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Fetching todos...")
  (let [res   (http/get "https://jsonplaceholder.typicode.com/todos"
                        {:query-params {}
                         :headers      {}})
        todos (-> res :body j/read-value)]
    (print-table todos)))
