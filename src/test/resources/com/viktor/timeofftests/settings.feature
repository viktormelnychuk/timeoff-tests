Feature: Edit company wide settings

  Background:
    Given default "admin" user is created

  Scenario Outline: Edit company settings
    Given I am on "Settings" page

    When I edit company settings with following:
    |  company_name  |  country  |  date_format  |  time_zone  |
    | <company_name> | <country> | <date_format> | <time_zone> |

    Then "company settings" page should reflect correct information

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

  Scenario Outline: Admin user edits leave type
    Given following leave type is created:
      |  name           |  color  |  use_allowance  | limit   |
      | <original_name> | color 1 | true            | 10      |

    And I am on "Settings" page

    When I edit "<original_name>" leave type to:
    |  name  |  color  |  use_allowance  | limit   |
    | <name> | <color> | <use_allowance> | <limit> |

    Then "leave types" page should reflect correct information

    Examples:
      |original_name |  name     |  color  |  use_allowance  | limit   |
      | Holiday      | Something | color 2 | false           | 20      |
      | Leave        |           | color 3 | false           | 20      |

  Scenario: Admin user can add new leave type
    Given  I am on "Settings" page

    When I add new leave type:
      |  name  |  color  |  use_allowance  | limit   |
      | 123    | color 2 | true            | 20      |

    Then "leave types" page should reflect correct information
    And "123" leave type should be present on new absence popup

  Scenario: Admin can delete leave type
    Given I am on "Settings" page

    When I delete "Holiday" leave type

    Then "leave types" page should reflect correct information
    And "Holiday" leave type should not be present on new absence popup

  Scenario: All bank holidays are displayed
    When I am on "Settings" page

    Then displayed bank holidays match holidays in db

  Scenario: Add new bank holiday
     Given I am on "Settings" page

     When I add new bank holiday:
     | name        | date       |
     | new holiday | 10-10-2018 |

     Then displayed bank holidays match holidays in db

  Scenario: Edit bank holiday name
    Given I am on "Settings" page

    When I edit bank holiday name

    Then displayed bank holidays match holidays in db

  Scenario: Delete one bank holiday
    Given I am on "Settings" page

    When I delete one bank holiday

    Then displayed bank holidays match holidays in db

  Scenario: Import default holidays
    Given I am on "Settings" page

    When I delete multiple bank holiday

    And import default holidays

    Then displayed bank holidays match holidays in db

  Scenario: Delete company
    Given I am on "Settings" page

    When I delete company with name "Acme"

    Then company with name "Acme" is deleted
    And the "Login" page should be opened

  Scenario: Cannot delete company with invalid name
    Given I am on "Settings" page
    When I delete company with name "Does not exist"

    Then company with name "Acme" is not deleted
