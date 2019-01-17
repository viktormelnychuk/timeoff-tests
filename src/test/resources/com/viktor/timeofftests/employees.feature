Feature: Check the employees page

  Background:
    Given default "admin" user is created

  Scenario: Employess page reflects correct information
    Given following users are created:
      | email              | password |
      | tester2@viktor.com | 1234     |
      | tester3@viktor.com | 1234     |

    And following leaves are created:
      | user_email         | leave_type | amount_of_days | status   |
      | tester@viktor.com  | Holiday    | 5              | approved |
      | tester2@viktor.com | Holiday    | 8              | approved |
      | tester3@viktor.com | Holiday    | 2              | approved |

    Then "employees" page should reflect correct information

  Scenario Outline: Filter employees by department
    Given following departments are created:
      | name         |  num_of_users |
      | Department 1 |  5            |
      | Department 2 |  0            |
      | Department 3 |  10           |

    And I am on "employees" page

    When I filter list of employees by "<department_name>" department

    Then employees page should display users that belong to "<department_name>" department

    Examples:
     | department_name |
     | Department 1    |
     | Department 2    |
     | Sales           |
     | All departments |

  Scenario Outline: I create new employee
    Given I am on "employees" page
    And following departments are created:
    | name         |
    | Department 1 |

    When I create an employee with following:
     | first_name   | last_name   | email   | department     | started_on   | ended_on   | password   | auto_approve   | admin   |
     | <first_name> | <last_name> | <email> | <department>   | <started_on> | <ended_on> | <password> | <auto_approve> | <admin> |

    Then "employees" page should reflect correct information

    And I can login as user with "<email>" email and "<password>" password

    Examples:
      | first_name | last_name | email            | department   | started_on | ended_on | password | auto_approve | admin |
      | James      | Smith     | james@viktor.com | Sales        | today      |          | 1234     | true         | true  |
      | James      | Smith     | james@viktor.com | Department 1 | in past    |          | 1234     | false        | true  |
      | James      | Smith     | james@viktor.com | Department 1 | today      |          | 1234     | false        | false  |