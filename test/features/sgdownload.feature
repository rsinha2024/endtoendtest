Feature: SG Download job to download patner files and send back to each partner
  Scenario: Download all the files for all the partners provided for a specific date. (parameters - list(partnerIBID), trade date)
    Given Default location of the incoming file is $bucket/$wlp/inbound, can be overwritten using a config table
    Then Upload the processed data into the postgres database (agent_loan_requests)
    And Move the file to archive folder once processed
    And Track the files received and processing. (agent_load_file_tracking)

