(ns api.sftp-client
  (:import (org.apache.sshd.client SSHClient)
           (org.apache.sshd.sftp.client SftpClient)
           (java.nio.file Files Paths)))

(defn upload-file
  [host port username password local-file-path remote-file-path]
  (let [client (SSHClient.)]
    (try
      ;; Set up SSH client with password-based authentication
      (doto client
        (.setPasswordAuthenticator
          (reify org.apache.sshd.client.auth.password.PasswordAuthenticator
            (authenticate [_ _ _] true)))  ;; Always authenticate (for simplicity, use static password)
        (.connect host port))

      (let [sftp-client (.createSftpClient client)]
        ;; Upload file directly from local file path to remote file path
        (.put sftp-client (Paths/get local-file-path) (Paths/get remote-file-path)))

      (finally
        (.disconnect client)))))
