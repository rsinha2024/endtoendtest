Feature: We need to download and load the End of Month File
  Scenario: Download the monthly accrual file and store to db
    Given A monthly accrual file in the format BILLING_ALLOCATION_MONTHLY.yyyymmdd.hhmmss.csv
    Given Uploaded to the s3 folder  S3 bucket that stores the partners sftp files,env.drivewealth.sftp/freetrade/inbound
    Then The job will parse the file and save the data in agent_lending._monthly_accrual table
    And The job should move the file to archive folder after successful process


