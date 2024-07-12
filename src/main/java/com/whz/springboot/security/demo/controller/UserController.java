package com.whz.springboot.security.demo.controller;

import com.whz.springboot.security.demo.model.MingYueUser;
import com.whz.springboot.security.demo.service.MingYueUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/** @author Strive */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final MingYueUserService mingYueUserService;

    /**
     * 获取用户信息
     * 请求：http://localhost:8080/user/admin
     *
     * @param username
     * @return
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<MingYueUser> queryUserByName(@PathVariable String username) {
        return ResponseEntity.ok(mingYueUserService.queryUserByName(username));
    }
}
