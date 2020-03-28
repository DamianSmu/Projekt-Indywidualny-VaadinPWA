package com.projekt.vaadinpwa.backend.repository;

import com.projekt.vaadinpwa.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
