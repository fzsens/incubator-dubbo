package org.apache.dubbo.demo.provider.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("users")
public interface UserService {

    @GET
    @Path("register")
     void registerUser(String userName);
}
