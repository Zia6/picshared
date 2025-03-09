//package com.yupi.yupicturebackend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/picture/edit").permitAll()  // 允许 WebSocket 连接
//                .anyRequest().authenticated()
//            )
//            .csrf(csrf -> csrf.disable());  // 关闭 CSRF，否则 WebSocket 可能会被拦截
//        return http.build();
//    }
//}
