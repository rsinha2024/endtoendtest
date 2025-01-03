Feature: SG Upload job to upload status files and send back to each partner
  Scenario: Generate status file and upload to S3
    Given some transactions in agent_loan_requests table in settled, rejected or cancelled status
    And in not sent status
    Then generate loan status file
    And update sent_to_agent column to Y

