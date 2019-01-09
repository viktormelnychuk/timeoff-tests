Feature: Signup as new user

  Scenario: Password and password confirmation should be same
    Given I am on "Register" page
    When I signup as:
      | company  | email             | first_name  | last_name | password | password_confirmation |
      | Acme     | tester@viktor.com | John        | Doe       | 1234     | 4321                  |

    Then I should see alert "Confirmed password does not match initial one" on the page
    And I should be on "Register" page
    And user "tester@viktor.com" should not be present in database

#add different timezones and conutries
  Scenario: Signup as new user with default timezone and country

    Given I am on "Register" page

    When I signup as:
    | company  | email             | first_name  | last_name | password |
    | Acme     | tester@viktor.com | John        | Doe       | 1234     |

    Then company with name "Acme" should be present in database
    And department with name "Sales" should be present in database
    And user "tester@viktor.com" should be in "Acme" company and "Sales" department
    And database should have session associated with "tester@viktor.com"

  Scenario Outline: Cannot signup as exisitng user
    Given default company with name "Acme" is created
    And default department "Sales" in "Acme" company is created
    And following user is created:
    |  email  |  password  |  admin  |
    | <email> | <password> | <admin> |
    And I am on "Register" page

    When I signup as:
    | company | email    | first_name | last_name | password |
    | Acme1    | <email> | John       | Doe       | 1234     |

    Then I should see alert "<alert>" on the page
    And I should be on "Register" page
    And company with name "Acme1" should not be present in database

    Examples:
    | email             | password | admin | alert |
    | tester@viktor.com | 1234     | true  | Failed to register user please contact customer service. Error: Email is already used      |
    | tester@viktor.com | 1234     | false | Failed to register user please contact customer service. Error: Email is already used      |
