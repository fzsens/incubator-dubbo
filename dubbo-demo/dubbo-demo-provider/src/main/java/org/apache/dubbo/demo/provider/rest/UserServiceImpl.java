package org.apache.dubbo.demo.provider.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(UserService.class);

    public void registerUser(String userName) {
        // save the user...
        System.out.println(userName);
        log.info("receive msg " + userName);
    }
}
