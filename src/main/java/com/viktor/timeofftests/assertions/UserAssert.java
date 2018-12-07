package com.viktor.timeofftests.assertions;

import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import org.assertj.core.api.AbstractObjectAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAssert extends AbstractObjectAssert<UserAssert, User> {

    private UserService userService = UserService.getInstance();

    public UserAssert (User actual){
        super(actual, UserAssert.class);
    }

    public UserAssert isAdmin(){
        isNotNull();
        User userInDb = userService.getUserWithEmail(actual.getEmail());
        if(!userInDb.isAdmin()){
            failWithMessage("User with email <%s> is not admin but expected to be", actual.getEmail());
        }
        return this;
    }

    public UserAssert isDepartmentBoss(){
        isNotNull();
        Department department = DepartmentService.getInstance().getDepartmentWithId(actual.getDepartmentID());
        if(department.getBossId() != actual.getId()){
            failWithMessage("Expected department[name=<%s>] admin user[<%s>] but got user[%s]",department.getName(),
                    actual.getEmail(),department.getBossId());
        }
        return this;
    }
}
