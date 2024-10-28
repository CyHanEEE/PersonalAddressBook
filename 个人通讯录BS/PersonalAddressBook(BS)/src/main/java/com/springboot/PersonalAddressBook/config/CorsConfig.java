package com.springboot.PersonalAddressBook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//表示这是配置类
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration conf = new CorsConfiguration();
        //允许哪些请求源跨域访问，比如http://localhost:8080，如果允许所有的请求源跨域访问，就用*
        conf.addAllowedOriginPattern("*");
        //允许所有的请求头跨域访问
        conf.addAllowedHeader("*");
        //允许所有的请求方式跨域访问，如post、get等
        conf.addAllowedMethod("*");
        //允许前端请求携带认证信息，如果前端打开了AllowCredentials设置，则后台服务也必须打开AllowCredentials
        conf.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",conf);

        return new CorsFilter(source);
    }
}
