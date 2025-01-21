Feature: Create job to download PAO file from Loanet SFTP Server
  Scenario: Download loanet file from sftp server
    Given Loanet file pao3360 in sftp server folder /LOANET/EOD
    Then Upload the processed data into the postgres database (loanet_open_loans)

