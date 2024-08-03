package com.sri.jwt.springang.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface myuserepo extends JpaRepository<myuser,Long> {

    public Optional<myuser> findByusername(String username);

}
