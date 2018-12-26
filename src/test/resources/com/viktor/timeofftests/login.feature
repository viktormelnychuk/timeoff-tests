Feature: Tests that user can login
  Background:
    Given default company with name "Acme" is created
    Given default department "Sales" in "Acme" company is created

    @positive @func
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

    @negative  @func
  Scenario: Deactivated user cannot login
    Given following user is created:
    | email             | password | activated |
    | tester@viktor.com | 1234     | false     |

    When I login as user "tester@viktor.com" with password "1234"
    Then database should not have session associated with "tester@viktor.com"
    And I should see alert "Incorrect credentials" on the page

    @positive @func
  Scenario: Not registered user cannot login
    When I login as user "tester12@viktor.com" with password "1234"
    Then database should not have session associated with "tester12@viktor.com"
    And I should see alert "Incorrect credentials" on the page