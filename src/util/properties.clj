(ns util.properties
  (:import (java.io FileInputStream)
           (java.util Properties)))

(defn load-properties
  [file-path]
  (let [props (Properties.)]
    (with-open [stream (FileInputStream. file-path)]
      (.load props stream))
    props))

(defn get-property
  [props key]
  (.getProperty props key))

;; Usage:
(def properties (load-properties "resources/config.properties"))
(defn prop [name]
  (get-property properties name))

