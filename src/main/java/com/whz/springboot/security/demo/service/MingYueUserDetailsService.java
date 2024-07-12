package com.whz.springboot.security.demo.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.whz.springboot.security.demo.model.MingYueUser;
import com.whz.springboot.security.demo.vo.LoginUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Strive
 * @date 2022/4/22 14:53
 * @description
 */
@Slf4j
@Component
public class MingYueUserDetailsService implements UserDetailsService {

    @Resource
    private MingYueUserService mingYueUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StrUtil.isBlank(username)) {
            log.info("登录用户：{} 不存在", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }

        // 查出密码
        MingYueUser user = mingYueUserService.queryUserByName(username);
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }

        return new LoginUserVo(user);
    }
}
