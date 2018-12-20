Feature: Tests that user can login
  Background:
    Given company with name "Acme" is created

  Scenario Outline: Test that different users can login
    Given following users are created:
      | email   | password   | admin   | activated   |
      | <email> | <password> | <admin> | <activated> |

    Examples:
      | email | password | admin | activated |
