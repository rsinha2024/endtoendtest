(ns api.sftp_client

  (:require
            [util.properties :as p])
  (:import (com.jcraft.jsch JSch ChannelSftp Session)
           (java.util Properties)
           (java.io File)
           (java.util Vector)
           ))


(defn create-session
  [host port username password]
  (let [jsch (JSch.)
        session (.getSession jsch username host port)]
    ;; Set the password
    (.setPassword session password)
    ;; Set strict host key checking to no (for simplicity)
    (.setConfig session "StrictHostKeyChecking" "no")
    session))

(defn create-session-with-key
  [host port username private-key]
  (let [jsch (JSch.)
        session (.getSession jsch username host port)]
    (.addIdentity jsch private-key)
    ;; Set strict host key checking to no (for simplicity)
    (.setConfig session "StrictHostKeyChecking" "no")
    session))

(defn connect
  [host port username password]
  (let [session (create-session host port username password)]
    (.connect session)
    (let [channel (.openChannel session "sftp")]
      (.connect channel)
      {:session session
       :channel channel})))

  (defn connect-with-key
    [host port username private-key]
    (let [session (create-session-with-key host port username private-key)]
      (.connect session)
      (let [channel (.openChannel session "sftp")]
        (.connect channel)
        {:session session
         :channel channel})))

    (defn cd
      [sftp-channel path]
      (println "Changing directory to" path)
      (.cd sftp-channel path))

(defn put1
  "Uploads a local file to a remote file path using SFTP."
  [sftp-channel local-file-path remote-file-path]
  (try
    (let [local-file (java.io.File. local-file-path)]
      (if (.exists local-file)
        (do
          (println "Uploading file" local-file-path "to" remote-file-path)
          (.put sftp-channel local-file-path remote-file-path)
          (println "Upload successful"))
        (do
          (println "Error: Local file does not exist:" local-file-path)
          (throw (Exception. (str "Local file does not exist: " local-file-path))))))

    (catch com.jcraft.jsch.SftpException e
      (println "SFTP error during file upload:" (.getMessage e))
      (.printStackTrace e)  ;; Print the full stack trace for debugging
      (throw e))            ;; Rethrow the exception if you want to propagate it

    (catch Exception e
      (println "General error during file upload:" (.getMessage e))
      (.printStackTrace e)
      (throw e))))

    (defn get
      [sftp-channel remote-file-path local-file-path]
      (println "Downloading file" remote-file-path "to" local-file-path)
      (.get sftp-channel remote-file-path local-file-path))

    (defn list-all
      [sftp-channel]
      (let [entries (.ls sftp-channel "*")]
        (map #(.getFilename %) entries)))

    (defn list-all-in-dir
      [sftp-channel path]
      (cd sftp-channel path)
      (list-all sftp-channel))

    (defn disconnect
      [{:keys [session channel]}]
      (when channel
        (.disconnect channel))
      (when session
        (.disconnect session))
      (println "Disconnected from the SFTP server"))

    (defn list-all-with-file-matcher
      [sftp-channel file-pattern]
      (let [entries (.ls sftp-channel file-pattern)]
        (map #(.getFilename %) entries)))

(defn check-remote-dir-exists
  [sftp-channel remote-dir]
  (try
    (let [files (list-all sftp-channel)]
      (println "Directory exists:" remote-dir)
      files)
    (catch com.jcraft.jsch.SftpException e
      (println e "Remote directory does not exist:" remote-dir)
      (throw e))))

(defn ensure-remote-dir
  [sftp-channel remote-dir]
  (try
    (check-remote-dir-exists sftp-channel remote-dir)
    (catch com.jcraft.jsch.SftpException _
      (println "Directory does not exist. Creating:" remote-dir)
      (.mkdir sftp-channel remote-dir)
      (check-remote-dir-exists sftp-channel remote-dir))))


(defn move-file
  "Move a file from one remote path to another."
  [sftp-channel source-file-path target-file-path]
  (try
    (println "Moving file from" source-file-path "to" target-file-path)
    (.rename sftp-channel source-file-path target-file-path)
    (println "File moved successfully")
    (catch com.jcraft.jsch.SftpException e
      (println e "Error moving file from" source-file-path "to" target-file-path)
      (throw e))))

(defn put-stream
  [sftp-channel input-stream remote-file-path]
  (println "Uploading stream to" remote-file-path)
  (.put sftp-channel input-stream remote-file-path))



(defn example-usage [host port user_name password local_file remote_file]
      (let [{:keys [session channel]} (connect host port user_name password)]
        (try
          ;; Upload a file
          ;(put1 channel local_file remote_file)
          (let [{:keys [session channel]} (connect host port user_name password)]
            (try
              (let [input-stream (java.io.FileInputStream. "resources/output/pao3360")]
                (put-stream channel input-stream "/LOANET/EOD/pao3360"))
              (catch com.jcraft.jsch.SftpException e
                (println "Directory does not exist. Creating:" e))
              (finally
                (disconnect {:session session :channel channel}))))

          ;; Download a file
          ;  (get channel remote_file local_file )

          ;; List all files in the current directory
          (println (list-all channel))

          ;; Change directory and list files
          (println (list-all-in-dir channel "/LOANET/EOD"))

          (finally
            (disconnect {:session session :channel channel})))))

(defn upload-file1
  [sftp-channel local-file-path remote-file-path]
  (try
    (println "Uploading file" local-file-path "to" remote-file-path)
    (.put sftp-channel local-file-path remote-file-path)
    (println "File uploaded successfully")
    (catch com.jcraft.jsch.SftpException e
      (println "Error during file upload:" (.getMessage e))
      (.printStackTrace e)  ;; Print full stack trace
      (throw e))))


(defn upload_file []
  (let [host (p/prop "SFTP_LOANET_HOST")
        username (p/prop "SFTP_LOANET_USER")
        password (p/prop "SFTP_LOANET_PASSWORD")
        local_path (p/prop "SFTP_LOCAL_FOLDER_PATH")
        remote_path (p/prop "SFTP_REMOTE_FOLDER_PATH")]
    ;(sftp-put host 22 username password local_path remote_path)
    (example-usage host 22 username password local_path remote_path)
    ))