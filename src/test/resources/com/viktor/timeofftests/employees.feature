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
    Then "employees" page should reflect correct information