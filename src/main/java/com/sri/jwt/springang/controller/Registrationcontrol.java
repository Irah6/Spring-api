package com.sri.jwt.springang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sri.jwt.springang.model.myuser;
import com.sri.jwt.springang.model.myuserdetailservice;
import com.sri.jwt.springang.model.myuserepo;


@RestController
public class Registrationcontrol {
    @Autowired
    private myuserepo myuserrepository;
    @Autowired
    private PasswordEncoder passwordEncoderr;
    @Autowired
    private myuserdetailservice userserv;

    @PostMapping("/register/user")
    public myuser registerUser(@RequestBody myuser user) {
        user.setPassword(passwordEncoderr.encode(user.getPassword()));
        return myuserrepository.save(user);
    }
    @PostMapping("/login/user")
    public ResponseEntity<String> login(@RequestBody myuser user) {
         try {
            // Authenticate user with provided username and password
            UserDetails userDetails = userserv.authenticate(user.getUsername(), user.getPassword());
            
            // You can perform additional actions here upon successful authentication, such as generating JWT token
            
            return ResponseEntity.ok("Login successful");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    
}
