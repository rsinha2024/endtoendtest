Feature: API to fetch positions from inteliclear
  Scenario: Fetch positions from INTC for a specific date
    Given some active loan positions in inteliclear
    Then fetch the positions from inteliclear
    And  and insert them into agent lending database
