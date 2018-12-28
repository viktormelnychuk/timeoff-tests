package com.viktor.timeofftests.steps;


import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Session;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.SessionService;
import com.viktor.timeofftests.services.UserService;
import lombok.extern.log4j.Log4j2;

import static org.junit.Assert.*;

@Log4j2
public class SessionSteps {

    private World world;
    private SessionService sessionService;
    private UserService userService;
    public SessionSteps(World world, SessionService sessionService, UserService userService){
        this.world = world;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public void sessionPresent (String email) throws Exception {
        String sidFromCookies = DriverUtil.getSidFromCookies(world.driver);
        Session session = sessionService.getSessionWithSid(sidFromCookies);
        String data = session.getData();
        User user = userService.getUserWithEmail(email);
        assertTrue("session does not contains user id",data.contains(String.valueOf(user.getId())));
    }

    public void sessionIsNotPresent(String email) throws Exception {
        String sidFromCoockie = DriverUtil.getSidFromCookies(world.driver);
        Session session = sessionService.getSessionWithSid(sidFromCoockie);
        String data = session.getData();
        assertFalse("session contains user id", data.contains("passport"));
    }
}
