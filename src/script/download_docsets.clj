(ns script.download-docsets
  (:require [clojure.data.xml :as xml]
            [clojure.data.zip.xml :refer [text xml->]]
            [clojure.zip :as zip]
            [clojure.java.io :as io]
            [me.raynes.fs.compression :refer [untar]]
            [me.raynes.fs :refer [mkdirs delete-dir]])
  (:import (java.util.zip GZIPInputStream)))

(assert
  (.isDirectory (io/file "./feeds"))
  "./feeds must exist (submodule of github/kapeli/feeds)")

(assert
  (> (->> (.list (io/file "./feeds"))
          (filter #(clojure.string/ends-with? % ".xml"))
          count)
     0)
  "./feeds must contain > 0 xml files")

(defn gunzip
  "Writes the contents of input to output, decompressed.
  input: something which can be opened by io/input-stream.
      The bytes supplied by the resulting stream must be gzip compressed.
  output: something which can be copied to by io/copy."
  [input output & opts]
  (with-open [input (-> input io/input-stream GZIPInputStream.)]
    (apply io/copy input output opts)))

(def sources (->> "./feeds"
                  io/file
                  file-seq
                  (filter #(clojure.string/ends-with? % ".xml"))))

(defn get-first-source-url [source-file]
  (with-open [in (io/input-stream source-file)]
    (-> (xml/parse in)
        zip/xml-zip
        (xml-> :entry :url text)
        first)))

(def tarfiles-path "./docsets/tarfiles")
(defn tarfile-path [filename]
  (format "./docsets/tarfiles/%s" filename))

(defn docset-path [filename]
  (format "./docsets/%s" filename))


(defn -main []
  (mkdirs tarfiles-path)

  (let [sources (map #(vector (clojure.string/replace (.getName %) #".xml$" "")
                              (get-first-source-url %))
                     sources)]
    (doall (pmap #(gunzip (second %)
                          (io/file (tarfile-path (first %))))
                 sources)))

  (let [tarfiles (.list (io/file tarfiles-path))]
    (doall (pmap #(untar (tarfile-path %)
                         (docset-path (name %)))
                 tarfiles))

    (delete-dir tarfiles-path)))
