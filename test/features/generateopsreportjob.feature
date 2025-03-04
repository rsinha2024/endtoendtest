Feature: Generate Ops report to move shares from 2402 to 3360
  Scenario: Generate a report for newly added loan/recall transactions before upload them to Inteliclear
    Given Some newly added loan recall transactions in agent lending database.
    Given query condition is trade_type=loan or recall, status=new, sent_to_intc != true, trade_date=$trade_date
    Given sum(quantity) group by cusip, trade_type
    Then The job produce a report of above transactions,
    And  The report should not include transactions that has been sent in previous run.
