(ns api.s3
  (:require [amazonica.aws.s3 :as s3]
            [clojure.java.io :as io]
            [util.properties :as p])
  (:import (java.io File)))

(defn upload-file
  "Uploads a file to an S3 bucket."
  [bucket-name file-path key]
  (let [file (io/resource file-path)]   ;; Accessing the file from resources
    (when file
      (println "In s3 uploading file")
      (s3/put-object :bucket-name bucket-name
                     :key key
                     :file (io/file file)))))  ;; Convert URL to File

(defn upload-test-file []
  (let [bucket-name "dev.drivewealth.sftp"
        resource-file "uploads/test1.txt"  ;; Path in the resources folder
        key "freetrade/inbound/test1.txt"]           ;; S3 destination key
    (upload-file bucket-name resource-file key)
    (println "File uploaded successfully!")))
