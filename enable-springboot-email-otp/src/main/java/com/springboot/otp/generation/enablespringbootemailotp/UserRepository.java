package com.springboot.otp.generation.enablespringbootemailotp;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserPojo, String> {
	UserPojo findByUsername(String username);
}
