package com.example.configserver.application.infrastruture.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers("/actuator/**")
            .antMatchers("/encrypt/**")
            .antMatchers("/decrypt/**")
        super.configure(web)
    }
}