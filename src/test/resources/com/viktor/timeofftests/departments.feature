Feature: Tests for departments
  Background:
    Given default "admin" user is created

  Scenario: Departments page reflects correct information

    Given following departments are created:
    | name         | allowance | include_pub_holidays | accrued_allowance | num_of_users |
    | Department 1 | 10        | true                 | true              | 5            |
    | Department 2 | 20        | false                | true              | 2            |
    | Department 3 | 5         | false                | false             | 5            |

    When I am on "Departments" page

    Then "departments" page should reflect correct information

  Scenario Outline: Create new department
    Given following users are created:
      | email              |
      | tester2@viktor.com |
      | tester3@viktor.com |

    And I am on "Departments" page

    When I create following department:
     | name   | allowance   | include_pub_holidays   | accrued_allowance   | boss   |
     | <name> | <allowance> | <include_pub_holidays> | <accrued_allowance> | <boss> |

    Then "departments" page should reflect correct information

    Examples:
      | name         | allowance | include_pub_holidays | accrued_allowance | boss               |
      | Department 1 | 10        | true                 | true              | tester@viktor.com  |
      | Department 2 | 20        | false                | true              | tester2@viktor.com |
      | Department 3 | 5         | false                | false             | tester3@viktor.com |

  Scenario Outline: Edit department
    Given following users are created:
      | email              |
      | tester2@viktor.com |
      | tester3@viktor.com |

    And following departments are created:
      | name         | allowance | include_pub_holidays | accrued_allowance | num_of_users |
      | Department 1 | 10        | true                 | true              | 5            |
      | Department 2 | 20        | false                | true              | 2            |
      | Department 3 | 5         | false                | false             | 5            |

    And I am on "<name>" department page

    When I edit "<name>" department with:
      | new_name   | allowance   | include_pub_holidays   | accrued_allowance   | secondary_approver   | manager   |
      | <new_name> | <allowance> | <include_pub_holidays> | <accrued_allowance> | <secondary_approver> | <manager> |

    Examples:
      |name          | new_name | allowance | include_pub_holidays | accrued_allowance | secondary_approver | manager            |
      | Department 1 | Dep21    | 20        | true                 | false             |  do one            | tester3@viktor.com |
