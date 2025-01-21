Feature: Get Settlement Status from Anetics Using New DW API
  Scenario: Utilize the Broadridge settlements API to ingest information previously contained in the settlement files uploaded by operations
    Given Records in the database api
    Then Update them based on Anetics response

