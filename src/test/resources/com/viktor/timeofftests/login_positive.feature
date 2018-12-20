Feature: Tests that user can login
  Background:
    Given default company with name "Acme" is created

  Scenario Outline: Test that different users can login
    Given following user is created:
      | email   | password   | admin   |
      | <email> | <password> | <admin> |

    When I login as user "<email>" with password "<password>"

    Then database should have session associated with "<email>"
    And the "Calendar" page should be opened

    Examples:
      | email              | password | admin |
      | tester@viktor.com  | 1234     | true  |
      | tester1@viktor.com | 1234     | false |

  Scenario: Deactivated user cannot login
    Given following user is created:
    | email             | password | activated |
    | tester@viktor.com | 1234     | false     |

    When I login as user "tester@viktor.com" with password "1234"
    Then database should have session associated with "tester@viktor.com"
    And the "Login" page should be opened