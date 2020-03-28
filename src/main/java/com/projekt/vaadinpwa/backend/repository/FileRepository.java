package com.projekt.vaadinpwa.backend.repository;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
