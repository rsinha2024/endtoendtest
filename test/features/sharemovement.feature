Feature: API to generate Inteliclear Share Movement File
  Scenario: Generate EOD share movement file for a specific date
    Given some transactions in agent_loan_requests table in settled status for a given trade date
    Then sharemovement file
    And file name should be in format eg DRVW_INTE_transactions_ordertrans_lending.txt

