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

    # rewrite this to check filtering without just restarting browser
    # For example: add step that accepts argument with separator (e.g. ":") and click on dep names one by one
    # validate page after each click

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
      | James      | Smith     | james@viktor.com | Department 1 | today      |          | 1234     | false        | false |

    # add LDAP tests here
  Scenario Outline: Cannot add new employee
    Given I am on "employees" page

    And following departments are created:
    | name         |
    | Department 1 |

    When I try to create an employee with following:
      | first_name   | last_name   | email   | department     | started_on   | ended_on   | password   | password_confirmation   |
      | <first_name> | <last_name> | <email> | <department>   | <started_on> | <ended_on> | <password> | <password_confirmation> |

    Then I should see alert "<alert>" on the page

    Examples:
      | first_name | last_name | email             | department   | started_on | ended_on | password | password_confirmation | alert                                         |
      | James      | Smith     | tester@viktor.com | Sales        | today      |          | 1234     | 1234                  | Email is already in use                       |
      | James      | Smith     | james@viktor.com  | Department 1 | in past    |          | 1234     | 4321                  | Confirmed password does not match initial one |
      | James      | Smith     | james@viktor.com  | Department 1 | today      | in past  | 1234     | 1234                  | End date for user is before start date        |

    Scenario Outline: Edit employee information
      Given following departments are created:
      | name         | num_of_users |
      | Department 1 | 2            |

      And following users are created:
      | email              | last_name           | first_name            | department            | admin             | auto_approve            | started_on          | ended_on           | password            |
      | <original_email>   | <original_last_name>| <original_first_name> | <original_department> | <original_admin>  | <original_auto_approve> |<orignal_started_on> | <orignal_ended_on> | <original_password> |

      And I am on "employees" page

      When I edit user with email "<original_email>" to have:
      | email   | last_name   | first_name   | department   | admin   | auto_approve   | started_on   | ended_on   | password   | password_confirmation   |
      | <email> | <last_name> | <first_name> | <department> | <admin> | <auto_approve> | <started_on> | <ended_on> | <password> | <password_confirmation> |

      Then "employees" page should reflect correct information

      Then user with email "<email>" should see correct info

      Examples:
        | original_email     | original_last_name  | original_first_name   | original_department   | original_admin    | original_auto_approve   |orignal_started_on   | orignal_ended_on   | original_password | email              | last_name   | first_name   | department   | admin   | auto_approve   | started_on   | ended_on   | password   | password_confirmation   |
        | tester2@viktor.com | James               | Brown                 | Department 1          | true              | true                    | in past             |                    | 1234              | tester3@viktor.com | Black       |  John        |              |         |                |              |            |            |                         |
        #| tester2@viktor.com | James               | Brown                 | Sales                 | false             | false                   | in past             |                    | 1234              |                    |             |              | Department 1 |         |                |              |            |            |                         |
        #| tester2@viktor.com | James               | Brown                 | Sales                 | false             | false                   | in past             |                    | 1234              |                    |             |              |              | true    |                |              |            |            |                         |
        #| tester2@viktor.com | James               | Brown                 | Sales                 | false             | false                   | in past             |                    | 1234              |                    |             |              |              |         |                |              |  today     |            |                         |
        #| tester2@viktor.com | James               | Brown                 | Sales                 | false             | false                   | in past             |                    | 1234              |                    |             |              |              |         |                |    in past   |            | 4321       | 4321                    |


