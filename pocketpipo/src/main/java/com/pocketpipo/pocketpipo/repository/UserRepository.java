package com.pocketpipo.pocketpipo.repository;


import com.pocketpipo.pocketpipo.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    public Optional<User> findByEmail(String email);

    public Optional<User> findByEmailAndDeletedFalse(String email);
}
