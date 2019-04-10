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
      #| Department 2 | 20        | false                | true              | tester2@viktor.com |
      #| Department 3 | 5         | false                | false             | tester3@viktor.com |

  Scenario Outline: Edit department

    Given following departments are created:
      | name         | allowance | include_pub_holidays | accrued_allowance | num_of_users |
      | Department 1 | 10        | true                 | true              | 5            |
      | Department 2 | 20        | false                | true              | 2            |
      | Department 3 | 5         | false                | false             | 5            |

    And following users are created:
      | email              | department   |
      | tester2@viktor.com | Department 2 |
      | tester3@viktor.com | Department 3 |

    And I am on "<name>" department page

    When I edit "<name>" department with:
      | new_name   | allowance   | include_pub_holidays   | accrued_allowance   | manager   |
      | <new_name> | <allowance> | <include_pub_holidays> | <accrued_allowance> | <manager> |

    Then "departments" page should reflect correct information

    Examples:
      |name          | new_name | allowance | include_pub_holidays | accrued_allowance | manager            |
      | Department 1 | Dep21    | 20        | false                | false             |                    |
      | Department 2 |          | 20        |                      | false             | tester3@viktor.com |
      | Department 3 | Dep21    | 20        | false                | false             | tester2@viktor.com |

  Scenario Outline: Add secondary supervisors

    Given following departments are created:
      | name         | multiple_supervisors | amount_of_secondary_supervisors | num_of_users |
      | Department 1 | do                   | 2                               | 3            |
      | Department 2 | do                   | 3                               | 6            |
      | Department 3 | do not do            |                                 | 4            |
      | Department 4 | do not do            |                                 | 10           |

    And I am on "<name>" department page

    When I "<action>" "<amount>" additional supervisors for department "<name>"

    Then "<name>" department page should reflect correct information

    Examples:
    | name         | action    | amount |
    | Department 1 | remove    | 1      |
    | Department 2 | remove    | 3      |
    | Department 3 | add       | 1      |
    | Department 4 | add       | 4      |



