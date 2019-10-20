package com.ho.learnlogindemo.controller;

import com.ho.learnlogindemo.bean.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    /**
     * 未登录用户/登录用户/管理员可访问的接口
     */
    @GetMapping("/anonymous/info")
    public String anonymous_info() {
        return "未登录用户/登录用户/管理员可访问的接口";
    }

    /**
     * 所有登录用户访问的接口
     */
    @GetMapping("/user/info")
    public User user_info() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 仅超级管理员可访问接口
     */
    @GetMapping("/admin/info")
    public User admin_info() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
