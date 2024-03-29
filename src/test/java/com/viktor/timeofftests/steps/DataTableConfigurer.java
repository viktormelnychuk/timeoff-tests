package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.common.DateUtil;
import com.viktor.timeofftests.forms.*;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pools.UserPool;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.*;

@SuppressWarnings("unused")
public class DataTableConfigurer implements TypeRegistryConfigurer {
    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(User.class, this::transformUser));
        typeRegistry.defineDataTableType(new DataTableType(SignupForm.class,this::transformToSignupForm));
        typeRegistry.defineDataTableType(new DataTableType(CompanySettingsForm.class, this::transformToForm));
        typeRegistry.defineDataTableType(new DataTableType(NewDepartmentForm.class, this::transformToNewDepartmentForm));
        typeRegistry.defineDataTableType(new DataTableType(WeeklyScheduleForm.class, this::transformToWeeklyScheduleForm));
        typeRegistry.defineDataTableType(new DataTableType(LeaveTypeForm.class, this::transformToLeaveTypeForm));
        typeRegistry.defineDataTableType(new DataTableType(InsertLeaveForm.class, this::transformToInsertLeaveForm));
        typeRegistry.defineDataTableType(new DataTableType(EmployeeForm.class, this::transformToNewEmployeeForm));
        typeRegistry.defineDataTableType(new DataTableType(EditEmployeeForm.class, this::transformToEditEmployeeForm));
    }

    private LeaveTypeForm transformToLeaveTypeForm (Map<String,String> entry){
        LeaveTypeForm form = new LeaveTypeForm();
        form.setName(entry.get("name"));
        if(StringUtils.isNotEmpty(entry.get("color"))){
            String color = "leave_type_" + StringUtils.replace(entry.get("color")," ","_");
            form.setColor(color);
        }

        form.setLimit(Integer.parseInt(entry.get("limit")));
        form.setPrimary(transformToBoolean(entry.get("primary"), false));
        form.setUseAllowance(transformToBoolean(entry.get("use_allowance"), true));
        return form;
    }

    private EmployeeForm transformToNewEmployeeForm (Map<String, String> entry){
        EmployeeForm form = new EmployeeForm();
        if(StringUtils.isEmpty(entry.get("first_name"))){
            form.setFirstName(UserPool.getName());
        } else {
            form.setFirstName(entry.get("first_name"));
        }
        if(StringUtils.isEmpty(entry.get("last_name"))){
            form.setLastName(UserPool.getLastName());
        } else {
            form.setLastName(entry.get("last_name"));
        }
        if(StringUtils.isEmpty(entry.get("email"))){
            form.setEmail(UserPool.getEmail());
        } else {
            form.setEmail(entry.get("email"));
        }
        if(StringUtils.isEmpty(entry.get("department"))){
            form.setDepartmentName(Constants.DEFAULT_DEPARTMENT_NAME);
        } else {
            form.setDepartmentName(entry.get("department"));
        }
        form.setAdmin(transformToBoolean(entry.get("admin"), false));
        form.setAutoApprove(transformToBoolean(entry.get("auto_approve"), false));
        if(StringUtils.isEmpty(entry.get("started_on"))){
            form.setStartedOn(LocalDate.now());
        } else {
           form.setStartedOn(DateUtil.selectDateBasedOnString(entry.get("started_on")));
        }
        if(StringUtils.isEmpty(entry.get("ended_on"))){
            form.setEndedOn(null);
        } else {
            form.setEndedOn(DateUtil.selectDateBasedOnString(entry.get("ended_on")));
        }
        if(StringUtils.isEmpty(entry.get("password"))){
            form.setPassword(Constants.DEFAULT_USER_PASSWORD);
            form.setPasswordConfirmation(Constants.DEFAULT_USER_PASSWORD);
        } else if(StringUtils.isNotEmpty(entry.get("password")) && StringUtils.isEmpty(entry.get("password_confirmation"))) {
            form.setPassword(entry.get("password"));
            form.setPasswordConfirmation(entry.get("password"));
        } else {
            form.setPassword(entry.get("password"));
            form.setPasswordConfirmation(entry.get("password_confirmation"));
        }
        return form;
    }
    private EditEmployeeForm transformToEditEmployeeForm(Map<String,String> entry){
        EditEmployeeForm form = new EditEmployeeForm();
        form.setFirstName(entry.get("first_name"));
        form.setLastName(entry.get("last_name"));
        form.setPassword(entry.get("password"));
        form.setPasswordConfirmation(entry.get("password_confirmation"));
        form.setEmail(entry.get("email"));
        form.setDepartmentName(entry.get("department"));
        form.setAdmin(transformToBoolean(entry.get("admin"), false));
        form.setAutoApprove(transformToBoolean(entry.get("auto_approve"), false));
        form.setStartedOn(DateUtil.selectDateBasedOnString(entry.get("started_on")));
        form.setEndedOn(DateUtil.selectDateBasedOnString(entry.get("ended_on")));
        return form;
    }
    private InsertLeaveForm transformToInsertLeaveForm (Map<String, String> entry){
        InsertLeaveForm form = new InsertLeaveForm();
        form.setUserEmail(entry.get("user_email"));
        form.setLeaveName(entry.get("leave_type"));
        form.setAmountOfDays(Integer.parseInt(entry.get("amount_of_days")));
        form.setStatus(entry.get("status"));
        form.setApproverComment(entry.get("approver_comment"));
        form.setEmployeeComment(entry.get("employee_comment"));
        String decidedAt = entry.get("decided_at");
        if(StringUtils.isEmpty(decidedAt)){
            form.setDecidedAt(LocalDate.now());
        } else {
            form.setDecidedAt(LocalDate.parse(decidedAt));
        }
        return form;
    }
    private WeeklyScheduleForm transformToWeeklyScheduleForm(Map<String, String> entry){
        WeeklyScheduleForm form = new WeeklyScheduleForm();
        form.setMonday(transformToIntForWeeklySchedule(entry.get("monday")));
        form.setTuesday(transformToIntForWeeklySchedule(entry.get("tuesday")));
        form.setWednesday(transformToIntForWeeklySchedule(entry.get("wednesday")));
        form.setThursday(transformToIntForWeeklySchedule(entry.get("thursday")));
        form.setFriday(transformToIntForWeeklySchedule(entry.get("friday")));
        form.setSaturday(transformToIntForWeeklySchedule(entry.get("saturday")));
        form.setSunday(transformToIntForWeeklySchedule(entry.get("sunday")));

        return form;
    }
    private NewDepartmentForm transformToNewDepartmentForm(Map<String, String> entry) {
        NewDepartmentForm form = new NewDepartmentForm();
        form.setName(entry.get("name"));
        if(StringUtils.isNotEmpty(entry.get("allowance"))){
            form.setAllowance(Integer.parseInt(entry.get("allowance")));
        } else {
            form.setAllowance(Constants.DEFAULT_DEPARTMENT_ALLOWANCE);
        }
        form.setPublicHolidays(transformToBoolean(entry.get("include_pub_holidays"), true));
        form.setAccruedAllowance(transformToBoolean(entry.get("accrued_allowance"), false));
        form.setNumberOfUsers(transformToInt(entry.get("num_of_users")));
        if(StringUtils.equals(entry.get("multiple_supervisors"), "do")){
            form.setSecondarySupervisors(true);
        }
        if(StringUtils.isNotEmpty(entry.get("amount_of_secondary_supervisors"))){
            form.setAmountOfSecondarySupervisors(Integer.parseInt(entry.get("amount_of_secondary_supervisors")));
        }
        return form;
    }
    private CompanySettingsForm transformToForm(Map<String, String> entry){
        CompanySettingsForm form = new CompanySettingsForm();
        form.setCompanyName(entry.get("company_name"));
        form.setCountry(entry.get("country"));
        form.setDateFormat(entry.get("date_format"));
        form.setTimezone(entry.get("time_zone"));
        if(StringUtils.isNotEmpty(entry.get("company"))){
            form.setCompanyName(entry.get("company"));
        } else {
            form.setCompanyName(StringUtils.EMPTY);
        }
        return form;
    }
    private SignupForm transformToSignupForm(Map<String, String> entry){
        SignupForm form = new SignupForm();
        String companyName = entry.get("currentCompany");
        String firstName = entry.get("first_name");
        String lastName = entry.get("last_name");
        String email = entry.get("email");
        String password = entry.get("password");
        String passwordConfirmation = entry.get("password_confirmation");
        String country = entry.get("country");
        String timezone = entry.get("timezone");
        if(companyName == null){
            companyName = Constants.DEFAULT_COMPANY_NAME;
        }
        if (firstName == null){
            firstName = Constants.DEFAULT_USER_NAME;
        }
        if(lastName == null){
            lastName = Constants.DEFAULT_USER_LAST_NAME;
        }
        if(email == null){
            email = Constants.DEFAULT_USER_EMAIL;
        }
        if(password == null){
            password = Constants.DEFAULT_USER_PASSWORD;
        }
        if(passwordConfirmation == null){
            passwordConfirmation = Constants.DEFAULT_USER_PASSWORD;
        }

        if(country == null){
            country = Constants.DEFAULT_COMPANY_COUNTRY;
        }
        if(timezone == null){
            timezone = Constants.DEFAULT_COMPANY_TIMEZONE;
        }
        form.setCompanyName(companyName);
        form.setFirstName(firstName);
        form.setLastName(lastName);
        form.setEmail(email);
        form.setPassword(password);
        form.setPasswordConfirmation(passwordConfirmation);
        form.setCountry(country);
        form.setTimezone(timezone);
        return form;
    }
    private User transformUser (Map<String, String> entry){
        try {
            String email = entry.get("email");
            String password = entry.get("password");
            String firstName = entry.get("first_name");
            String lastName = entry.get("last_name");
            boolean activated = transformToBoolean(entry.get("activated"), true);
            boolean admin = transformToBoolean(entry.get("admin"), false);
            boolean autoApprove = transformToBoolean(entry.get("auto_approve"), true);
            LocalDate startDate = transformToDate(entry.get("started_on"));
            LocalDate endDate = transformToDate(entry.get("ended_on"));
            String departmentName = entry.get("department");
            String companyName = entry.get("currentCompany");
            if (email == null) {
                email = UserPool.getEmail();
            }
            if(password == null){
                password = "1234";
            }
            if (firstName == null) {
                firstName = UserPool.getName();
            }
            if (lastName == null) {
                lastName = UserPool.getLastName();
            }
            if(!activated){
                Calendar calendar = Calendar.getInstance();
                calendar.roll(Calendar.DAY_OF_YEAR, -10);
                // Roll 10 days back from today
                startDate = LocalDate.now().minusDays(10);
                // Roll 9 days from startedOn
                endDate = LocalDate.now().minusDays(1);
            }
            if(startDate == null){
                startDate = LocalDate.now();
            }
            User user = new User.Builder()
                    .withEmail(email)
                    .withName(firstName)
                    .withLastName(lastName)
                    .withPassword(password)
                    .activated(activated)
                    .admin(admin)
                    .autoApproved(autoApprove)
                    .startedOn(startDate)
                    .endedOn(endDate)
                    .build();
            user.setDepartmentName(departmentName);
            user.setCompanyName(companyName);
            return user;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private boolean transformToBoolean(String s, boolean defaultNull){
        if(s == null){
            return defaultNull;
        }
        return Objects.equals(s, "true");
    }
    private int transformToIntForWeeklySchedule(String s){
        if(Objects.equals(s,"true")){
            return 1;
        } else {
            return 2;
        }
    }

    private LocalDate transformToDate(String s){
        if(s == null){
            return null;
        } else {
            // add logic to parse string
            return LocalDate.parse(s);

        }

    }

    private int transformToInt (String s){
        if(s==null){
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }
}
