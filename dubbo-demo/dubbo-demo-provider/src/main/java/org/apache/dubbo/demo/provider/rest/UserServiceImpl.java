package org.apache.dubbo.demo.provider.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("users")
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(UserService.class);

    @GET
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    public void registerUser(String userName) {
        // save the user...
        log.info("receive msg " + userName);
    }
}
