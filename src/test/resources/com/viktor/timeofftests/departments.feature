Feature: Tests for departments
  Background:
    Given default "tester@viktor.com" user is created
    And default company with name "Acme" is created
    And default department "Sales" in "Acme" company is created

  Scenario: Departments page reflects correct information

    Given following departments are created:
    | name         | allowance | include_pub_holidays | accrued_allowance | num_of_users |
    | Department 1 | 10        | true                 | true              | 5            |
    | Department 2 | 20        | false                | true              | 2            |
    | Department 3 | 5         | false                | false             | 5            |

    When I am on "Departments" page

    Then departments page reflects correct information

  Scenario Outline:
    Given I am on "Departments" page

    When I create following department:
     | name   | allowance   | include_pub_holidays   | accrued_allowance   |
     | <name> | <allowance> | <include_pub_holidays> | <accrued_allowance> |

    Then departments page reflects correct information

    Examples:
      | name         | allowance | include_pub_holidays | accrued_allowance |
      | Department 1 | 10        | true                 | true              |
      | Department 2 | 20        | false                | true              |
      | Department 3 | 5         | false                | false             |