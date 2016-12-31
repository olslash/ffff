(ns app.db
  (:require [mount.core :refer [defstate]]
            [conman.core :as conman]
            [me.raynes.fs :refer [list-dir hidden?]]
            [clojure.algo.generic.functor :refer [fmap]]

            [app.conf :refer [config]]))

(defn get-loaded-docset-names []
  (->> (list-dir "docsets")
       (map #(.getName %))
       (filter (complement hidden?))))

(defn docset-db-path [name]
  (format
    "docsets/%s/%s.docset/Contents/Resources/docSet.dsidx"
    name name))

(defn pool-spec [config db-path]
  {:jdbc-url (format "jdbc:sqlite:%s" db-path)})

(defn create-db-connections [docset-names config]
  (let [db-path-map (apply hash-map (interleave (map keyword docset-names)
                                                (map #(docset-db-path %)
                                                     docset-names)))]
    ;; map paths -> *db* connections to that path
    (fmap #(with-meta (conman/connect! (pool-spec config %))
                      {:dynamic true})
          db-path-map)))

    ;(fmap #(conman/bind-connection (pool-spec config %))
    ;      db-path-map)))

(defn disconnect-db-connections [db-path-map]
  (fmap #(conman/disconnect! %) db-path-map))

;; generated functions must be passed a conn object
(conman/bind-connection nil "sql/doc.sql")

(defstate db
  :start (create-db-connections (get-loaded-docset-names) config)
  :stop (disconnect-db-connections db))

