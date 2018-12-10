# Notes
1. Write test cases to automate
    * Login as admin (verify admin site loads)
    * Login as non-admin (verify admin site loads)
    * Try to login as deactivated user
2. Secret for generating session is :
```
val = session uid;
return val + '.' + crypto
    .createHmac('sha256', secret)
    .update(val)
    .digest('base64')
    .replace(/\=+$/, '');
};

```
session id generated with uid(24) where uid is [https://www.npmjs.com/package/uid-safe]

1. Figure out how to make sure order does not matter when creating user (inCompany and inDepartment can be used in any order)
2. Run all migrations! it is important!
3. ~~Rewrite to use assertAll when there are multiple asserts per test method~~ [DONE]
4. Examine and improve logging of all steps taken (e.g when using findOrCreate company it is not indicated that company was not fund)
# Features
1. Prepare poll of 2 or 3 drivers to get from so they are prepared when user tries to run tests [NEXT]
2. Make all tests parametrized! get test data from the csv/json/yml/xml (Use Junit5 parametrization and arguments + arguments resolver)(**may not be needed**)
3. Add Allure as reporting tool [LATER]

# Things to consider
1. Make db clearing run on before class instead of before method (increase performance?, allow to process same preconditons in @BeforeAll)
2. ~~Move to AssertJ~~ [NOPE]

# !!!Very important!!!
ADD NEW LAYER: Add layer of test steps (e.g. UserSteps should create new user in db in necessary, contain assertions via `validate*` methods