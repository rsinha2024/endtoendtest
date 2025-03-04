Feature: API to upload loan transactions
  Scenario: Generate loan transaction file and upload to AOD S3 bucket
    Given  Given records in agent_loan_requests table in status =new and sent_to_anetics !=true transactions
    Then Generate a loan transaction file
    And Save a copy of the file to S3 bucket env.drivewealth.agentlending/yyyymmdd
    And The filename pattern is loan_transactions_yyyymmdd_001.csv

