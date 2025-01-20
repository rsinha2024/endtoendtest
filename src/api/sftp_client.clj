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

    (defn put
      [sftp-channel local-file-path remote-file-path]
      (println "Uploading file" local-file-path "to" remote-file-path)
      (.put sftp-channel local-file-path remote-file-path))

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


    (defn example-usage [host port user_name password local_file remote_file]
      (let [{:keys [session channel]} (connect host port user_name password)]
        (try
          ;; Upload a file
          ; (put channel local_file remote_file)

          ;; Download a file
          (get channel remote_file local_file )

          ;; List all files in the current directory
          (println (list-all channel))

          ;; Change directory and list files
          (println (list-all-in-dir channel "/LOANET/EOD"))

          (finally
            (disconnect {:session session :channel channel})))))
(defn upload_file []
  (let [host (p/prop "SFTP_LOANET_HOST")
        username (p/prop "SFTP_LOANET_USER")
        password (p/prop "SFTP_LOANET_PASSWORD")
        local_path (p/prop "SFTP_LOCAL_FOLDER_PATH")
        remote_path (p/prop "SFTP_REMOTE_FOLDER_PATH")]
    ;(sftp-put host 22 username password local_path remote_path)
    (example-usage host 22 username password "pao_from_server" remote_path)
    ))