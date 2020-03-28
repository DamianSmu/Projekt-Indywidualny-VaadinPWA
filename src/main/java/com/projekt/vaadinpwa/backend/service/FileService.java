package com.projekt.vaadinpwa.backend.service;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.projekt.vaadinpwa.backend.entity.UserEntity;
import com.projekt.vaadinpwa.backend.repository.FileRepository;
import com.projekt.vaadinpwa.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {

    private FileRepository fileRepository;
    private UserRepository userRepository;
    private S3ConnectionService s3ConnectionService;

    //TODO
    private UserEntity testUser;

    public FileService(FileRepository fileRepository, UserRepository userRepository, S3ConnectionService s3ConnectionService) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.s3ConnectionService = s3ConnectionService;
    }

    public List<FileEntity> findAll() {
        return fileRepository.findAll();
    }

    @PostConstruct
    public void generateTestData() {
        if (userRepository.count() == 0) {
            generateTestUser();
        }
    }

    public void generateTestUser() {
        UserEntity user = new UserEntity();
        user.setUserName("TestUser");
        user.setEmail("testuser@gmail.com");
        testUser = user;
        userRepository.save(user);
    }


    public byte[] downloadFile(String path, String name) {
        return s3ConnectionService.downloadFile(path, name);
    }

    public void uploadFile(String fileName, String path, InputStream inputStream, Long contentLength, UserEntity owner) {
        try {
            s3ConnectionService.uploadFile(fileName, path, inputStream, contentLength);
        } catch (Exception e) {
            return;
        }
        FileEntity file = new FileEntity();
        file.setName(fileName);
        file.setPath(path);
        //TODO
        file.setOwner(testUser);
        fileRepository.save(file);
    }
}
