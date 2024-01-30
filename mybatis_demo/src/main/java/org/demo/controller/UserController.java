package org.demo.controller;

import org.demo.entity.User;
import org.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ClassName UserController
 * @description: TODO
 * @author: suhaoran
 * @date 2023年07月14日
 * @version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping("/insert")
    public String insert(){
        User user = new User();
        user.setAge(6);
        user.setName("哆啦A梦");
        user.setBirthday(new Date());
        userService.insert(user);
        return "success";
    }

}
