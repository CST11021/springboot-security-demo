package com.whz.springboot.security.demo.service;

import com.whz.springboot.security.demo.model.MingYueUser;
import org.springframework.stereotype.Service;

/** @author Strive */
@Service
public class MingYueUserService {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public MingYueUser queryUserByName(String username) {
        // mock db用户数据
        MingYueUser user = new MingYueUser();
        user.setUserId(1L);
        user.setUsername(username);
        user.setPassword("123456");

        return user;
    }
}
