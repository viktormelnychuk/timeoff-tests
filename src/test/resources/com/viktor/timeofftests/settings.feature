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

  Scenario Outline: Edit weekly schedule
    Given I am on "Settings" page

    When I edit weekly schedule to:
    | monday   | tuesday   |  wednesday  |  thursday  |  friday  |  saturday  |  sunday  |
    | <monday> | <tuesday> | <wednesday> | <thursday> | <friday> | <saturday> | <sunday> |

    Then "weekly schedule settings" page should reflect correct information

    Examples:
      |  monday  |  tuesday  |  wednesday  |  thursday  |  friday  |  saturday  |  sunday  |
      | true     | true      | true        | true       | true     | true       | true     |
      | false    | false     | false       | false      | false    | false      | false    |
      | true     | true      | true        | true       | true     | false      | false    |
      | true     | true      | false       | true       | true     | false      | false    |

  Scenario: Admin user edits leave type
    Given I am on "Settings" page

    When I edit leave type to:
    |  name  |  color  |  use_allowance  | limit   |
    | <name> | <color> | <use_allowance> | <limit> |

    Then "leave types" page should reflect correct information


  Scenario: Admin user  can add new leave type
    Given  I am on "Settings" page

    When I add new leave type:
      |  name  |  color  |  use_allowance  | limit   |
      | 123    | red     | true            | 20      |

    Then "leave types" page should reflect correct information
    
    
  Scenario: Admin user cannot delete all leave types
    Given I am on "Settings" page
    
    When I delete all leave types
    
    Then I should see alert "Cannot delete all leave types" on the page