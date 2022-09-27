package com.atguigu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.PasswordAuthentication;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启controller的权限控制
public class MySpringSecurityConfig extends WebSecurityConfigurerAdapter {


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.inMemoryAuthentication().withUser("user").password(new BCryptPasswordEncoder().encode("123456")).roles("");
//
//
//    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){

       return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http.headers().frameOptions().sameOrigin();


        //配置匿名访问的资源
        http.authorizeRequests().antMatchers("/static/**","/login").permitAll().anyRequest().authenticated();
        //自定义登录页面
        http.formLogin().loginPage("/login").defaultSuccessUrl("/");
        //登出
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login");
        //关闭跨域请求伪造
        http.csrf().disable();

        //去没有权限的页面控制
        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());
    }
}
