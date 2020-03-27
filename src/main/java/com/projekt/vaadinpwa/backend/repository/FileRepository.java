package com.projekt.vaadinpwa.backend.repository;

import com.projekt.vaadinpwa.backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
