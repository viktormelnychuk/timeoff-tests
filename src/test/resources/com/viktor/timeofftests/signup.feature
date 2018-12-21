Feature: Signup as new user

  Scenario: Signup as new user with default timezone and country

    Given I am on "Register" page

    When I signup as:
    | company  | email             | first_name  | last_name | password |
    | Acme     | tester@viktor.com | John        | Doe       | 1234     |

    Then company with name "Acme" should be present in database
    And department with name "Sales" should be present in database
    And admin user with email "tester@viktor.com" should be present in database
    And database should have session associated with "tester@viktor.com"