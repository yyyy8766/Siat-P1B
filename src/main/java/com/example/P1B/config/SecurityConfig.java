package com.example.P1B.config;

import com.example.P1B.domain.Role;
import com.example.P1B.handler.AuthFailureHandler;
import com.example.P1B.handler.AuthSuccessHandler;
import com.example.P1B.service.CustomizeUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomizeUserDetailsService customizeUserDetailsService;

    public SecurityConfig(CustomizeUserDetailsService customizeUserDetailsService) {
        this.customizeUserDetailsService = customizeUserDetailsService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customizeUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers( "/api/users/**", "/api/login/**").permitAll()
                .antMatchers("/users/").hasAuthority(Role.USER.getValue())
                .antMatchers("/guest/").hasAuthority(Role.ANONYMOUS.getValue())
                .and()
                .formLogin()
                    .loginProcessingUrl("/api/login/login")
                    .permitAll()
                .successHandler(new AuthSuccessHandler()) // 성공 핸들러 사용 설정
                .failureHandler(new AuthFailureHandler()) // 실패 핸들러 사용 설정
                .and()
                .logout()
                .logoutUrl("/users/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/")
                .and()
                .anonymous().authorities(Role.ANONYMOUS.getValue()); // 로그인하지 않은 사용자는 ANONYMOUS 공인
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}