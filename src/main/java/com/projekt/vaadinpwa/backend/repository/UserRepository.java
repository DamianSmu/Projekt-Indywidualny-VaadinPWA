package com.projekt.vaadinpwa.backend.repository;

import com.projekt.vaadinpwa.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
