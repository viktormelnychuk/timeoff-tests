Feature: Is it Friday?

  Scenario: Sunday is not Friday
    Given today is Sunday
    When I ask whether it is Friday
    Then I should be told "Nope"