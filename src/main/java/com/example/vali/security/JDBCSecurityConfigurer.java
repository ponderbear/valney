package com.example.vali.security;

import com.example.vali.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;


/**
 * 增加数据库查询的方式，就是为了隐藏在认证时显式的赋予登陆后用户的角色，改为从数据库中查询并获取
 * 认证：改用认证的provider，而构造provider，需要传入实现UserDetailsService的Detials类（返回security的User详情（用户及相应角色））
 * 授权：一样
 * */


/**
 * 执行过程：
 * 1、访问路径（/、等），判断是否授权访问该资源，没有授权（任何请求都需要先认证）则按照设定的loginForm到指定路径（/login）返回的页面登录以认证，并按照指定前端参数（name标记）获取login的name和password传递
 * 2、通过认证的方法configure中塞入的自定义provider（provider（一般用aoAuthenticationProvider）中塞入自定义并实现UserDetailsService接口的类，该自定义Service类中调用Repository，获取用户的角色（自定义），然后创建并返回Security中的User对象（包含了securiry设定的权限））；
 * 3、认证成功（从库里查到用户信息，并返回Security.User）后，调用授权方法中的SecessHandler的handle实现方法，根据入参Authentication获取用户的权限，做逻辑分支决定跳转的url，在response重定向
 */

@Configuration
public class JDBCSecurityConfigurer extends WebSecurityConfigurerAdapter {


    // 注入用户服务类
    @Resource
    private MyUserDetailsService myUserDetailsService;

    // 注入加密接口
    @Resource
    private PasswordEncoder passwordEncoder;

    // 注入用户认证接口
    @Resource
    private AuthenticationProvider authenticationProvider;

    // 注入认证处理成功类，验证用户成功后处理不同用户跳转到不同的页面
    @Resource
    NewAuthenticationSuccessHandler newAuthenticationSuccessHandler ;

    /*
     *  BCryptPasswordEncoder是Spring Security提供的PasswordEncoder接口是实现类
     *  用来创建密码的加密程序，避免明文存储密码到数据库
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider是Spring Security提供AuthenticationProvider的实现
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // 创建DaoAuthenticationProvider对象
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 不要隐藏"用户未找到"的异常
        provider.setHideUserNotFoundExceptions(false);
        // 通过重写configure方法添加自定义的认证方式。
        provider.setUserDetailsService(myUserDetailsService);
        // 设置密码加密程序认证
        provider.setPasswordEncoder(new NewPasswordEncoder());
        //返回构造的provider，
        return provider;
    }


    /**
     * 认证，改用自己实现的认证provider，provider里传入用户细节（即用户和相关联的角色），还有密码设置
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("AppSecurityConfigurer configure auth......");
        // 设置认证方式。
        auth.authenticationProvider(authenticationProvider);

    }

    /**
     * 授权
     *
     * 设置了登录页面，而且登录页面任何人都可以访问，然后设置了登录失败地址，也设置了注销请求，注销请求也是任何人都可以访问的。
     * permitAll表示该请求任何人都可以访问，.anyRequest().authenticated(),表示其他的请求都必须要有权限认证。
     *
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("AppSecurityConfigurer configure http......");
        http.authorizeRequests()
                // spring-security 5.0 之后需要过滤静态资源
                .antMatchers("/login","/css/**","/js/**","/img/*").permitAll()
                //这个hasRole判断默认是对填入的添加ROlE_去判断
                .antMatchers("/", "/home","/nestedException","/parentExceptionTest","/globalExceptionTest").hasRole("USER")
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "DBA")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").successHandler(newAuthenticationSuccessHandler)
                //接收传递的参数（相当与request.name,name是前端字段的name）
                .usernameParameter("loginName").passwordParameter("password")
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

}
