//
//    @Test
//    void cannotDeleteCompanyWithIncorrectName(){
//        Company currentCompany = companyService.getCompanyWithId(user.getCompanyID());
//        RemoveCompanyModal removeCompanyModal = generalSettingsPage.clickDeleteCompanyButton();
//        generalSettingsPage = removeCompanyModal
//                .fillCompanyName(currentCompany.getName()+"Random addition")
//                .clickDeleteButtonExpectingFailure();
//        String expectedMessage = "Failed to remove currentCompany. Reason: Provided name confirmation does not match currentCompany one";
//        assertAll(
//                ()->assertThat(generalSettingsPage.getAlertText(), is(expectedMessage)),
//                ()->assertThat(companyService.getCompanyWithId(currentCompany.getId()), is(notNullValue()))
//        );
//    }
//
//    @Test
//    void canDeleteCompanyWithCorrectName(){
//        Company currentCompany = companyService.getCompanyWithId(user.getCompanyID());
//        RemoveCompanyModal removeCompanyModal = generalSettingsPage.clickDeleteCompanyButton();
//        LoginPage loginPage = removeCompanyModal
//                .fillCompanyName(currentCompany.getName())
//                .clickDeleteButtonExpectingSuccess();
//        String expectedMessage = "Company Acme and related data were successfully removed";
//        assertAll(
//                ()->assertThat(generalSettingsPage.getAlertText(), is(expectedMessage)),
//                ()->assertThat(companyService.getCompanyWithId(currentCompany.getId()), is(nullValue())),
//                ()->assertThat(loginPage.getDriver().getCurrentUrl(), is(containsString(loginPage.getBaseUrl()))),
//                ()->assertThat(userService.getUserWithEmail(user.getEmail()), is(nullValue()))
//        );
//    }
//}
