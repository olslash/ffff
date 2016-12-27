(ns app.db
  (:require [mount.core :refer [defstate]]
            [conman.core :as conman]
            [app.conf :refer [config]]))

(defn pool-spec [config]
  {:jdbc-url "jdbc:sqlite:test.db"})


(defstate ^:dynamic *db*
          :start (conman/connect! (pool-spec config))
          :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/doc.sql")
