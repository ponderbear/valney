//package com.example.vali.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//
///**
// * 认证和授权的主要配置类
// */
//
//@Configuration
//public class SecurityConfigurer extends WebSecurityConfigurerAdapter{
//
//	/**
//	 * 注入认证处理类，处理不同用户跳转到不同的页面
//	 * */
//	@Autowired
//	NewAuthenticationSuccessHandler appNewAuthenticationSuccessHandler;
//
//	/**
//	 * 1 用户认证操作
//	 * 1.1实现WebSecurityConfigurerAdapter的configureGlobal方法
//	 * 1.2 Builder 的 inMemoryAuthentication为指定用户（jojo）赋予不同的权限（USER分组的权限）
//	 * 1.3使用自定义的密码存储器
//	 * */
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		System.out.println("AppSecurityConfigurer configureGlobal() 调用......，进行用户认证");
//		// spring-security 5.0 之后需要密码编码器，否则会抛出异常：There is no PasswordEncoder mapped for the id "null"
//		//AuthenticationManager
//		//Spring Security 在将用户name、password、roles自动保存到Authentication中时，默认role会加上ROLE_,则在认证成功的handler方法中做role判断时，需要添加ROLE_
//		auth.inMemoryAuthentication().passwordEncoder(new NewPasswordEncoder()).withUser("jojo").password("123456").roles("define_user");
//		auth.inMemoryAuthentication().passwordEncoder(new NewPasswordEncoder()).withUser("jack").password("admin").roles("define_user","DBA");
//	}
//
//
//	/**
//	 * 2.用户授权操作
//	 * 2.1 authorizeRequest设置授权操作，链接不同请求下，匹配不同的权限，permitAll，所有用户都可以访问；authenticaiton需要认证（登陆）才可以访问；
//	 * 2.2 formLogin，设置登陆操作，sucessHandler是登陆成功后的操作
//	 * 2.3 lgoout，设置注销操作
//	 * 2.4 异常操作，所有都是and链接
//	 * */
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//		System.out.println("AppSecurityConfigurer configure() 调用......，进行用户授权");
//		http.authorizeRequests()
//
//		// 重要！！！：/login,/css等映射，可以任何状态下、任何人访问(跳转至)，若是/或/home，则需要"user"角色权限，而user角色权限需要登录（认证）后进行（login=authenticated）
//				//而formLogin().login("/login")就是用来设置登陆（认证）的映射页面的（不符合要求则跳转到指定的认证登录页面）
//
//		.antMatchers("/login","/css/**","/js/**","/img/*").permitAll()
//	  	.antMatchers("/", "/home").hasRole("define_user")
//	  	.antMatchers("/admin/**").hasAnyRole("define_admin", "DBA")
//	  	.anyRequest().authenticated()//然后其余部分的授权，需要在登录后才可以进行（自动跳转至认证（登陆页面））
//
//	  	.and()//链接其他操作，successHandler，等认证成功，后嗲用Handler中重写的handler方法进行各种后续操作
//	  	.formLogin().loginPage("/login").successHandler(appNewAuthenticationSuccessHandler)
//	  	.usernameParameter("loginName").passwordParameter("password")//接收传递的参数
//
//	  	.and()
//	  	.logout().permitAll()
//	  	.and()
//				//对于登陆成功的用户，且访问的资源在授权禁止时，跳转到指定页面
////	  	.exceptionHandling().accessDeniedPage("/accessDenied");
//	  	.exceptionHandling().accessDeniedPage("/fileUpload");
//
//	}
//
//
//
//}
