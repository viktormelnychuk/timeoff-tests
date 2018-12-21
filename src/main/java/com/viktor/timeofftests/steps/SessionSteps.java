package com.viktor.timeofftests.steps;


import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Session;
import com.viktor.timeofftests.services.SessionService;
import lombok.extern.log4j.Log4j2;

import static org.junit.Assert.*;

@Log4j2
public class SessionSteps {

    private World world;
    private SessionService sessionService;
    public SessionSteps(World world, SessionService sessionService){
        this.world = world;
        this.sessionService = sessionService;
    }

    public void sessionPresent (String email) throws Exception {
        checkProvidedEmail(email);
        String sidFromCookies = DriverUtil.getSidFromCookies(world.driver);
        Session session = sessionService.getSessionWithSid(sidFromCookies);
        String data = session.getData();
        assertTrue("session does not contains user id",data.contains(String.valueOf(world.defaultUser.getId())));
    }

    public void sessionIsNotPresent(String email) throws Exception {
        checkProvidedEmail(email);
        String sidFromCoockie = DriverUtil.getSidFromCookies(world.driver);
        Session session = sessionService.getSessionWithSid(sidFromCoockie);
        String data = session.getData();
        assertFalse("session contains user id", data.contains(String.valueOf(world.defaultUser.getId())));
    }

    private void checkProvidedEmail (String email) throws Exception {
        if(!world.defaultUser.getEmail().equals(email)){
            log.error("User with email {} was not present in world", email);
            throw new Exception("User with email was not present in world");
        }
    }
}
