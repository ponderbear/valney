package com.example.vali.service;


import com.example.vali.entity.Role;
import com.example.vali.entity.User;
import com.example.vali.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 用来进行认证的具体过程，主要实现loadUserByUsername, "返回库里的用户和相应的权限"
 */
@Service
public class MyUserDetailsService implements UserDetailsService {


    // 注入持久层对象UserRepository
    @Autowired
    UserRepository userRepository;

    // 实现接口中的loadUserByUsername方法，通过该方法查询到对应的用户

    /**
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 调用持久层接口findByLoginName方法查找用户，此处的传进来的参数实际是loginName
        User user = userRepository.selectByUsernameJDBCSecurity(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 创建List集合，用来保存用户权限，GrantedAuthority对象代表赋予给当前用户的权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 获得当前用户权限集合
        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            // 将关联对象Role的authority属性转为为用户的认证权限,没办法，hasRole的判断自动要添加ROLE_来判断
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getAuthority()));
        }

        //注：此从库中获取的权限不像之前由authentication保存，若由其保存，会默认添加ROLE_,所以判断的时候需修改
        // 此处返回的是org.springframework.security.core.userdetails.User类，该类是Spring Security内部的实现
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
