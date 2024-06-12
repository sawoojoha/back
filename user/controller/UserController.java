package com.exameple.mytube.user.controller;

import com.exameple.mytube.user.domain.User;
import com.exameple.mytube.user.dto.Userdto;
import com.exameple.mytube.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public String join(@RequestBody User user) {
        System.out.println("email: " + user.getEmail());

        userService.join(user);
        return "user HOme";

    }

    @PostMapping("/login")
    public Userdto login(@RequestBody User user)throws Exception {
      Userdto loginedUser =   userService.login(user);
        return  loginedUser;
    }
}