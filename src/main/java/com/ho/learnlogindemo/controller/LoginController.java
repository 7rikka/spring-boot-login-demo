package com.ho.learnlogindemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    /**
     * 未登录访问需要登录的接口
     */
    @GetMapping("/login_page")
    public String login_page() {
        return "请先登录!";
    }

    /**
     * 注销成功
     */
    @GetMapping("/logout_success")
    public String logout_success() {
        return "注销成功!";
    }

    /**
     * 登录状态过期
     */
    @GetMapping("/session_invalid")
    private String session_invalid() {
        return "登录状态已过期!请重新登录!";
    }
}
