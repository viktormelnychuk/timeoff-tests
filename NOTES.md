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


# Features
1. Prepare poll of 2 or 3 drivers to get from so they are prepared when user tries to run tests