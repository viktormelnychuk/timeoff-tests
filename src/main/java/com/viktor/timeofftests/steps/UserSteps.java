package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.forms.EditEmployeeForm;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Log4j2
public class UserSteps {

    private UserService userService;
    private DepartmentService departmentService;

    public UserSteps(UserService userService, DepartmentService departmentService){
        this.userService = userService;
        this.departmentService = departmentService;
    }

    public void validateUserEdited(int userId, EditEmployeeForm form) {
        log.info("Validating user with id=[{}] have following {}", userId, form);
        User userToEdit = userService.getUserWithId(userId);
        if(StringUtils.isNotEmpty(form.getDepartmentName())){
            Department department = departmentService.getDepartmentWithNameAndCompanyId(form.getDepartmentName(), userToEdit.getCompanyID());
            assertThat(userToEdit.getDepartmentID(), is(department.getId()));
        }
        if(StringUtils.isNotEmpty(form.getFirstName())){
            assertThat(userToEdit.getName(), is(form.getFirstName()));
        }

        if(StringUtils.isNotEmpty(form.getEmail())){
            assertThat(userToEdit.getEmail(), is(form.getEmail()));
        }

        if(StringUtils.isNotEmpty(form.getLastName())){
            assertThat(userToEdit.getLastName(), is(form.getLastName()));
        }
        if(Objects.nonNull(form.getStartedOn())){
            assertThat(userToEdit.getStartDate(), is(form.getStartedOn()));
        }
        assertThat(userToEdit.getEndDate(), is(form.getEndedOn()));
        assertThat(userToEdit.isAdmin(), is(form.isAdmin()) );
        assertThat(userToEdit.isAutoApprove(), is(form.isAutoApprove()));
    }
}
