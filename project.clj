(defproject ffff "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-environ "1.0.3"]]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/algo.generic "0.1.2"]
                 [org.clojure/data.xml "0.2.0-alpha2"]
                 [org.clojure/data.zip "0.1.2"]

                 [me.raynes/fs "1.4.6"]
                 [environ "1.0.3"]

                 [http-kit "2.2.0"]
                 [cheshire "5.6.3"]
                 [http-kit.fake "0.2.1"]

                 [com.cemerick/url "0.1.1"]
                 [liberator "0.14.1"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-devel "1.5.0"]
                 [ring/ring-jetty-adapter "1.5.0"]
                 [bidi "2.0.8"]

                 [org.xerial/sqlite-jdbc "3.15.1"]
                 [conman "0.5.8"]
                 [robert/hooke "1.3.0"]

                 ;; logging
                 [com.taoensso/timbre "4.7.0"]
                 [com.fzakaria/slf4j-timbre "0.3.2"]        ;; req'd by migratus

                 [mount "0.1.11"]

                 [compojure "1.0.2"]]                        ;; req'd by liberator.dev


  ;; generate uberjars
  ;; `lein clean; lein with-profile core uberjar`
  :profiles {:uberjar {}

             :core    {:main         ^:skip-aot app.core
                       :uberjar-name "main.jar"}}

  :target-path "target/%s"
  :repl-options {:init-ns user})
