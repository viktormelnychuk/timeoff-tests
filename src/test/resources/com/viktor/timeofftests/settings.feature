Feature: Edit company wide settings

  Background:
    Given default "admin" user is created

  Scenario Outline: Edit company settings
    Given I am on "Settings" page

    When I edit company settings with following:
    |  company_name  |  country  |  date_format  |  time_zone  |
    | <company_name> | <country> | <date_format> | <time_zone> |

    Then "company settings" page should reflect correct information
    And database should contain edited company

    Examples:
      |  company_name  |  country  |  date_format  |  time_zone    |
      | Acme1          | CU        | DD MMM, YY    | Europe/Kiev   |
      |                | CU        |               |               |
      |                |           |               | Europe/Kiev   |
      | Acme1          |           |               |               |
      |                |           |               |               |