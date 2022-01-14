(defproject web-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/core.async "1.5.648"]
                 [ring/ring-core "1.9.4"]
                 [info.sunng/ring-jetty9-adapter "0.17.2"]
                 [org.eclipse.jetty/jetty-alpn-java-server "10.0.7"]
                 [metosin/reitit "0.5.15"]
                 [ring-cors "0.1.13"]]
  :main web-app.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot      :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"
                                  "-Dclojure.compiler.elide-meta=[:doc :file :line :added]"]}
             :dev     {:plugins [[lein-shell "0.5.0"]]}}
  :aliases {"native"     ["shell"
                          "native-image"
                          "--report-unsupported-elements-at-runtime"
                          "--initialize-at-build-time"
                          "--no-server"
                          "-jar" "./target/uberjar/${:uberjar-name:-${:name}-${:version}-standalone.jar}"
                          "-H:ReflectionConfigurationFiles=./resources/reflection.json"
                          "-H:Name=./target/${:name}"]
            "run-native" ["shell" "./target/${:name}"]})
