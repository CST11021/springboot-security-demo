package com.whz.springboot.security.demo.config;

import com.whz.springboot.security.demo.handler.LoginFailureHandler;
import com.whz.springboot.security.demo.handler.LoginSuccessHandler;
import com.whz.springboot.security.demo.service.MingYueUserDetailsService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import javax.annotation.Resource;

/**
 * Spring Security 配置
 *
 * 安全配置类需要使用 @EnableWebSecurity 注解修饰，该注解是一个组合注解，内部包含了 @Configuration 注解，所以安全配置类不需要添加 @Configuration 注解即可被 Spring 容器识别
 *
 * @author Strive
 * @date 2022/4/22 15:11
 * @description
 */
@EnableJdbcHttpSession
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MingYueUserDetailsService mingYueUserDetailsService;

    /** 登录成功的处理 */
    @Resource
    private LoginSuccessHandler loginSuccessHandler;
    /** 登录失败的处理 */
    @Resource
    private LoginFailureHandler loginFailureHandler;

    /** 配置认证方式等 */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(mingYueUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());

        // 采用内存存储方式，用户认证信息存储在内存中
        // auth.inMemoryAuthentication()
        //         .withUser("admin")
        //         .password(new BCryptPasswordEncoder().encode("123456"))
        //         .roles("ROLE_ADMIN");

    }

    /**
     * 定制一些全局性的安全配置，例如：不拦截静态资源的访问
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静态资源的访问不需要拦截，直接放行
        web.ignoring().antMatchers("/assets/**", "/css/**", "/images/**");
    }

    /**
     * http相关的配置，包括登入登出、异常处理、会话管理等
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /***** 一、登录表单的配置 *****/

        FormLoginConfigurer<HttpSecurity> formLoginConfigurer = http.formLogin();
        // 默认路径为/login，默认会去/resources/static目录下寻找login.html登录页面
        formLoginConfigurer.loginPage("/login.html");
        // 登录表单提交的请求路径，注意是POST请求
        formLoginConfigurer.loginProcessingUrl("/authorization/form");
        // // 登录表单的用户名参数
        // formLoginConfigurer.usernameParameter("");
        // // 登录表单的密码参数
        // formLoginConfigurer.passwordParameter("");
        //
        // // 登录成功重定向URL
        // formLoginConfigurer.successForwardUrl("");
        // // 登录失败重定向URL
        // formLoginConfigurer.failureForwardUrl("");

        // 自定义登录成功处理逻辑
        formLoginConfigurer.successHandler(loginSuccessHandler);
        // 自定义登录失败处理逻辑
        formLoginConfigurer.failureHandler(loginFailureHandler);

        // 允许所有用户
        formLoginConfigurer.permitAll();



        /***** 二、CSRF防护配置 */

        CsrfConfigurer<HttpSecurity> csrfConfigurer = http.csrf();
        // 这里需要注意 logoutSuccessURL失败的可能原因，是因为没有添加 http.csrf().disable(); 不加这个将只支持post方式的logout退出
        // 关闭 csrf 防护，因为对于我们的所有请求来说，都是需要携带身份信息的
        // 如果是基于token，是不需要csrf防护
        csrfConfigurer.disable();
        // 这里需要注意 开启了 csrf 防护 授权界面的 <input type="hidden" name="_csrf" th:value="${_csrf.token}"/> 是必须要添加的
        // 如果关闭了 csrf 防护，这个 <input type="hidden" name="_csrf" th:value="${_csrf.token}"/> 不需要添加



        /***** 三、认证配置 */

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequestConfig = http.authorizeRequests();
        // 放行接口
        authorizeRequestConfig.antMatchers("/login", "/oauth/**").permitAll();
        // 除上面外的所有请求全部需要鉴权认证
        authorizeRequestConfig.anyRequest().authenticated();


        /***** 跨域相关配置 */
        // http.cors();

        /***** 启用 HTTP Basic 认证 */
        // http.httpBasic();

    }
}
