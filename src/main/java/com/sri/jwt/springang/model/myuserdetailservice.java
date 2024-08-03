package com.sri.jwt.springang.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class myuserdetailservice implements UserDetailsService {

    @Autowired
    private myuserepo repository;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<myuser> userOptional = repository.findByusername(username);
        
        myuser user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(getRoles(user)) // Here you can define user roles based on your application logic
                .build();

    }
    
    private String[] getRoles(myuser user) {
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
    public UserDetails authenticate(String username, String password) {
        UserDetails userDetails;
        try {
        userDetails = loadUserByUsername(username);
    } catch (UsernameNotFoundException ex) {
        throw new UsernameNotFoundException("Invalid user");
    }

    if (passwordEncoder.matches(password, userDetails.getPassword())) {
        return userDetails;
    } else {
        throw new BadCredentialsException("Invalid password");
    }
    }
}
