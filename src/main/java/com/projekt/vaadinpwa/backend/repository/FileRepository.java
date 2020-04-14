package com.projekt.vaadinpwa.backend.repository;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByParentIsNull();

    List<FileEntity> findByParentIsNullAndDirectoryIsTrue();

    List<FileEntity> findByParent(FileEntity parent);

    List<FileEntity> findByParentAndDirectoryIsTrue(FileEntity parent);
}
