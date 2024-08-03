package com.sri.jwt.springang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sri.jwt.springang.model.myuserdetailservice;

@Configuration
@EnableWebSecurity
public class springsecurity {
    @Autowired
    private myuserdetailservice userdetailservice;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(registry->{
            registry.requestMatchers("/h2-console/**","/register/**","/login/**").permitAll();
            registry.requestMatchers("/admin/**").hasRole("Admin");
            registry.requestMatchers("/user/**").hasRole("User");
            registry.anyRequest().authenticated();
        })
        .formLogin(formLogin->formLogin.permitAll())
        .build();
    }

    // @Bean
    // public UserDetailsService userDetailsService() {
    //     UserDetails normalUser = User.builder()
    //         .username("gc")
    //         .password(passwordEncoder().encode("password"))  // Example password encoding
    //         .roles("User")
    //         .build();

    //     UserDetails adminUser = User.builder()
    //         .username("admin")
    //         .password(passwordEncoder().encode("password"))  // Example password encoding
    //         .roles("Admin", "User")
    //         .build();

    //     return new InMemoryUserDetailsManager(normalUser, adminUser);
    // }
    @Bean
    public UserDetailsService userDetailsService()
    {
        return userdetailservice;
    }
    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setUserDetailsService(userdetailservice);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
