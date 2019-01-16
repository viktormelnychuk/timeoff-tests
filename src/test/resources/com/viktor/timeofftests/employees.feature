Feature: Check the employees page

  Background:
    Given default "admin" user is created

  Scenario: Page reflects correct information
    Given following users are created:
    | email              | password |
    | tester2@viktor.com | 1234     |
    | tester3@viktor.com | 1234     |
    
    When I am on "employees" page
    
    Then "employees" page should reflect correct information

  Scenario: Some tests
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