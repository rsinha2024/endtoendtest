Feature: SG Download job to download patner files and send back to each partner
  Scenario: Partners field missing or invalid should cause the job to fail
    Given A bad request to SG download job and missing partner field
    Then The job should fail
