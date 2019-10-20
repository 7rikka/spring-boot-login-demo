package com.ho.learnlogindemo.service;

import com.ho.learnlogindemo.bean.Role;
import com.ho.learnlogindemo.bean.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (s.equals("admin")){
            //登录管理员用户并添加权限
            User admin=new User();
            admin.setId(1L);
            admin.setUsername("admin");
            //这里使用MD5加密
            admin.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            List<Role> roles=new ArrayList<>();
            roles.add(new Role(1L,"超级管理员"));
            admin.setRoles(roles);
            System.out.println("返回admin用户");
            return admin;
        }else if (s.equals("user")){
            //登录普通用户并添加权限
            User user=new User();
            user.setId(1L);
            user.setUsername("user");
            user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            List<Role> roles=new ArrayList<>();
            roles.add(new Role(2L,"普通用户"));
            user.setRoles(roles);
            System.out.println("返回user用户");
            return user;
        }else {
            //避免返回null
            System.out.println("返回null user");
            return new User();
        }
    }
}
