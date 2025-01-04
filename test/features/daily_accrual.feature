Feature: We need to download and load the daily accrual file.
  Scenario: Download the daily accrual file and store to db
    Given A daily accrual file in the format ACCRUALS.DAILY.yyyymmdd.hhmmss.csv
    Given Uploaded to the s3 folder  S3 bucket that stores the partners sftp files,env.drivewealth.sftp/freetrade/inbound
    Then The job will parse the file and save the data in agent_lending._daily_accural table
    And The job should move the file to archive folder after successful process


