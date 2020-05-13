package com.projekt.vaadinpwa.backend.service;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.projekt.vaadinpwa.backend.entity.UserEntity;
import com.projekt.vaadinpwa.backend.repository.FileRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {

    private FileRepository fileRepository;
    private UserService userService;
    private DataSourceService dataSourceService;

    public FileService(FileRepository fileRepository, UserService userService, DataSourceService dataSourceService) {
        this.fileRepository = fileRepository;
        this.userService = userService;
        this.dataSourceService = dataSourceService;
    }

    public List<FileEntity> findAll() {
        return fileRepository.findAll();
    }

    @PostConstruct
    public void generateTestData() {
        userService.saveUser("user", "testuser@gmail.com", "password");
        UserEntity testUser = userService.findByUserName("user").get();
    }

    public byte[] downloadFile(String path, String name) {
        return dataSourceService.downloadFile(path, name);
    }

    public void uploadFile(String fileName, String path, InputStream inputStream, Long contentLength, UserEntity owner, FileEntity directory) {
        try {
            dataSourceService.uploadFile(fileName, path, inputStream, contentLength);
        } catch (Exception e) {
            return;
        }
        FileEntity file = new FileEntity();
        file.setName(fileName);
        file.setPath(path);
        file.setOwner(owner);
        file.setDirectory(false);
        if(directory != null)
            file.setParent(directory);
        fileRepository.save(file);
    }

    public List<FileEntity> getRoot() {
        return fileRepository.findByParentIsNull();
    }

    public List<FileEntity> getDirRoot() {
        return fileRepository.findByParentIsNullAndDirectoryIsTrue();
    }

    public List<FileEntity> getChildren(FileEntity parent) {
        return fileRepository.findByParent(parent);
    }

    public List<FileEntity> getDirChildren(FileEntity parent) {
        return fileRepository.findByParentAndDirectoryIsTrue(parent);
    }

    public FileEntity createNewDirectory(String fileName, String path, FileEntity parent, UserEntity owner) {
        FileEntity file = new FileEntity();
        file.setName(fileName);
        file.setPath(path);
        file.setOwner(owner);
        file.setDirectory(true);
        file.setParent(parent);
        return fileRepository.save(file);
    }

    public void deleteFile(FileEntity fileEntity)
    {
        while (!getChildren(fileEntity).isEmpty())
            getChildren(fileEntity).forEach(this::deleteFile);
        dataSourceService.removeFile(fileEntity.getName(), fileEntity.getPath());
        fileRepository.delete(fileEntity);
    }
}
