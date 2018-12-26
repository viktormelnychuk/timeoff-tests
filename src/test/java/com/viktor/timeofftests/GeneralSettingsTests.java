//    @Test
//    void allHolidaysDisplayed(){
//        List<BankHoliday> displayed = generalSettingsPage.bankHolidaySettings.getAllDisplayedHolidays();
//        List<BankHoliday> inDb = BankHolidaysService.getInstance().getAllBankHolidaysForCompany(user.getCompanyID());
//        inDb.sort(Comparator.comparing(BankHoliday::getDate));
//        assertThat(displayed, hasAllItemsExcludingProperties(inDb,"id","companyId"));
//    }
//
//    @Test
//    void editBankHolidayName(){
//        BankHolidaySettings bankHolidaySettings = generalSettingsPage.bankHolidaySettings;
//        List<BankHoliday> displayed = bankHolidaySettings.getAllDisplayedHolidays();
//        int[] indexes = getRandomIndexes(displayed, displayed.size()/2);
//        GeneralSettingsPage generalSettingsPage =
//                bankHolidaySettings
//                .editMultipleHolidayNamesWithRandomString(indexes)
//                .clickSubmitButton();
//        displayed = generalSettingsPage.bankHolidaySettings.getAllDisplayedHolidays();
//        List<BankHoliday> inDb = BankHolidaysService.getInstance().getAllBankHolidaysForCompany(user.getCompanyID());
//        inDb.sort(Comparator.comparing(BankHoliday::getDate));
//        assertThat(displayed, hasAllItemsExcludingProperties(inDb, "id","companyId"));
//    }
//
//    @Test
//    void deleteMultipleBankHolidays(){
//        BankHolidaySettings bankHolidaySettings = generalSettingsPage.bankHolidaySettings;
//        GeneralSettingsPage generalSettingsPage =
//                bankHolidaySettings
//                .deleteMultipleHolidays(0,1,2,3)
//                .clickSubmitButton();
//        List<BankHoliday> displayed = generalSettingsPage.bankHolidaySettings.getAllDisplayedHolidays();
//        List<BankHoliday> inDb = BankHolidaysService.getInstance().getAllBankHolidaysForCompany(user.getCompanyID());
//        inDb.sort(Comparator.comparing(BankHoliday::getDate));
//        assertThat(displayed, hasAllItemsExcludingProperties(inDb, "id","companyId"));
//    }
//
//    @Test
//    void addNewBankHoliday(){
//        BankHolidaySettings bankHolidaySettings = generalSettingsPage.bankHolidaySettings;
//        GeneralSettingsPage generalSettingsPage = bankHolidaySettings
//                .clickAddNewButton()
//                .fillName("New Holiday")
//                .fillDate(new Date())
//                .clickCreateButton();
//        List<BankHoliday> displayed = generalSettingsPage.bankHolidaySettings.getAllDisplayedHolidays();
//        List<BankHoliday> inDb = BankHolidaysService.getInstance().getAllBankHolidaysForCompany(user.getCompanyID());
//        inDb.sort(Comparator.comparing(BankHoliday::getDate));
//        assertThat(displayed, hasAllItemsExcludingProperties(inDb, "id","companyId"));
//    }
//
//    @Test
//    void importDefaultBankHolidays(){
//        BankHolidaySettings bankHolidaySettings = generalSettingsPage.bankHolidaySettings;
//        List<BankHoliday> bankHolidays = bankHolidaySettings.getAllDisplayedHolidays();
//        int[] indexes = getRandomIndexes(bankHolidays, 4);
//        bankHolidaySettings.deleteMultipleHolidays(indexes);
//        List<String> deletedHolidays = bankHolidaySettings.getDeletedHolidays();
//        generalSettingsPage = generalSettingsPage.bankHolidaySettings.clickImportDefaultButton();
//        String expected = "New bank holidays were added: ";
//        assertThat(generalSettingsPage.getAlertText(), is(containsString(expected)));
//        assertThat(generalSettingsPage.getAlertText(), stringContainsAllSubstringsInAnyOrder(deletedHolidays));
//    }
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
