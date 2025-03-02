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

(defn list-s3-files1 [bucket-name prefix]
  (let [objects (s3/list-objects :bucket-name bucket-name :prefix prefix)]
    (println "objects " objects)
    (map :key (:object-summaries objects))))

;; Function to list objects under a specific prefix in the bucket
(defn list-s3-files [bucket-name prefix]
  (let [objects (s3/list-objects :bucket-name bucket-name :prefix prefix)]
    (println "Object summaries from S3 response:" (:object-summaries objects)) ; Added log for debugging
    (:object-summaries objects)))

;; Function to copy a file within the same bucket but to a new prefix (archive folder)
(defn copy-s3-file [bucket-name source-key target-key]
  (s3/copy-object :sourcebucketname bucket-name  ; Explicitly specify source-bucket
                  :source-key source-key
                  :destinationbucketname bucket-name
                  :destination-key target-key))

;; Function to delete files in S3 based on object keys
(defn delete-s3-files [bucket-name object-keys]
  (doseq [key object-keys]
    (s3/delete-object :bucket-name bucket-name :key key)))

;; Main function to move files from the source prefix to the archive prefix
(defn move-s3-files-to-archive [bucket-name prefix archive-prefix]
  (let [object-summaries (list-s3-files bucket-name prefix)]
    (if (> (count object-summaries) 0)
      (do
        (println "Files found, moving to archive..." object-summaries)
        (doseq [object object-summaries]
          (println "object=" object )
          (println "prefix=" prefix )
          (println "sk=" (:key object))
          (let [source-key (:key object)
                target-key (if source-key
                             (str archive-prefix (subs source-key (count prefix))) ;; Safely create target key
                             (do (println "Warning: No valid source key for" object) nil))]
            ;; Only proceed if source-key is valid and not a folder prefix
            (when (and source-key (not= source-key archive-prefix))  ;; Skip folder prefixes like "archive/"
              ;; Copy the file to the archive folder
              (copy-s3-file bucket-name source-key target-key)
              (s3/delete-object :bucket-name bucket-name :key source-key)
              ;; Delete the original file after copying
              (println "Moved" source-key "to" target-key))))
        (println "Files moved to archive."))
      (println "No files found to move."))))

