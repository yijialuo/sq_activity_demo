package com.sq.demo.controller;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class Login {
    @RequestMapping("/check_login")
    public boolean dologin(String username,String password){
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService=processEngine.getIdentityService();
        return identityService.checkPassword(username,password);
    }
}
