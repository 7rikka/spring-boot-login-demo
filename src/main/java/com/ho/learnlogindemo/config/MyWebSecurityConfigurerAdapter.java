package com.ho.learnlogindemo.config;

import com.ho.learnlogindemo.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class MyWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Bean
    AccessDeniedHandler getAccessDeniedHandler() {
        return new AuthenticationAccessDeniedHandler();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(
                        new PasswordEncoder() {
                            //定义密码加密的方法
                            @Override
                            public String encode(CharSequence charSequence) {
                                return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
                            }
                            //定义密码验证的方法
                            @Override
                            public boolean matches(CharSequence charSequence, String s) {
                                System.out.println("明文:"+charSequence);
                                System.out.println("密文:"+s);
                                boolean equals = s.equals(DigestUtils.md5DigestAsHex(charSequence.toString().getBytes()));
                                System.out.println("密码比对结果:"+equals);
                                return equals;
                            }
                        }
                );

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //设定各接口的访问权限
                .authorizeRequests()
                    //anonymous/info所有访客均可访问(未登录/已登录/超级管理员)
                    .antMatchers("/anonymous/info").permitAll()
                    .antMatchers("/session_invalid").permitAll()
                    //user/info登录用户可访问(已登录的普通用户/超级管理员用户)
                    .antMatchers("/user/info").authenticated()
                    //admin/info只有超级管理员可以访问
                    .antMatchers("/admin/info").hasRole("超级管理员")
                    //除以上接口，其他接口登录即可访问
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    //未登录访问需要登录的接口时，自动跳转到的地址
                    .loginPage("/login_page")
                    //登录成功
                    .successHandler(
                            new AuthenticationSuccessHandler() {
                                @Override
                                public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
                                    httpServletResponse.setContentType("application/json;charset=utf-8");
                                    PrintWriter out = httpServletResponse.getWriter();
                                    out.write("{\"status\":\"success\",\"msg\":\"登录成功\"}");
                                    out.flush();
                                    out.close();
                                }
                            }
                    )
                    //登录失败
                    .failureHandler(
                            new AuthenticationFailureHandler() {
                                @Override
                                public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
                                    httpServletResponse.setContentType("application/json;charset=utf-8");
                                    PrintWriter out = httpServletResponse.getWriter();
                                    out.write("{\"status\":\"error\",\"msg\":\"登录失败\"}");
                                    out.flush();
                                    out.close();
                                }
                            }
                    )
                    //登录表单post的地址
                    .loginProcessingUrl("/login")
                    //用户名的表单名,默认username
                    .usernameParameter("username")
                    //密码的表单名,默认password
                    .passwordParameter("password")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    //用户注销成功后跳转到的地址,如不配置则自动跳转到登录地址/login
                    .logoutSuccessUrl("/logout_success")
                    .permitAll()
                    .and()
                .sessionManagement()
                    //配置登录session过期后跳转到的地址
                    .invalidSessionUrl("/session_invalid")
                    .and()
                .csrf()
                    .disable()
                    .exceptionHandling()
                    //配置权限不足后访问的Handler
                    .accessDeniedHandler(getAccessDeniedHandler());
    }

    @Override
    public void configure(WebSecurity web) {
        //配置直接放行的地址，一般是静态资源的url
        web.ignoring().antMatchers("/static/**");
    }
}
