(ns app.core
  (:require [app.conf :refer [config]]
            [app.db]
            [app.util.logging :refer [do-logging-config]]

            [mount.core :as mount])
  (:gen-class))


(defn -main []
  (dosync
    (mount/start #'config)
    (do-logging-config config)
    (mount/start)))
