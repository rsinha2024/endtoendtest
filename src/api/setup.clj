(ns api.setup
  (:require [api.s3 :as s3]))
(s3/upload-file ["dev.drivewealth.sftp/freetrade/inbound"

                 ])
