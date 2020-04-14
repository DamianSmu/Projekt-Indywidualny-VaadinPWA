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
    private DataSourceService DataSourceService;

    public FileService(FileRepository fileRepository, UserService userService, DataSourceService DataSourceService) {
        this.fileRepository = fileRepository;
        this.userService = userService;
        this.DataSourceService = DataSourceService;
    }

    public List<FileEntity> findAll() {
        return fileRepository.findAll();
    }

    @PostConstruct
    public void generateTestData() {
        userService.saveUser("user", "testuser@gmail.com", "password");
        UserEntity testUser = userService.findByUserName("user").get();

        FileEntity fileA = new FileEntity();
        fileA.setOwner(testUser);
        fileA.setName("fileA.mp4");
        fileA.setPath("");
        fileA.setParent(null);
        fileA.setDirectory(false);
        fileRepository.save(fileA);


        FileEntity dirA = new FileEntity();
        dirA.setOwner(testUser);
        dirA.setName("dirA");
        dirA.setPath("dirA/");
        dirA.setParent(null);
        dirA.setDirectory(true);
        fileRepository.save(dirA);

        FileEntity fileB = new FileEntity();
        fileB.setOwner(testUser);
        fileB.setName("fileB.pdf");
        fileB.setPath(dirA.getPath() + "/" + "fileB.pdf");
        fileB.setParent(dirA);
        fileB.setDirectory(false);
        fileRepository.save(fileB);
    }

    public byte[] downloadFile(String path, String name) {
        return DataSourceService.downloadFile(path, name);
    }

    public void uploadFile(String fileName, String path, InputStream inputStream, Long contentLength, UserEntity owner) {
        try {
            DataSourceService.uploadFile(fileName, path, inputStream, contentLength);
        } catch (Exception e) {
            return;
        }
        FileEntity file = new FileEntity();
        file.setName(fileName);
        file.setPath(path);
        file.setOwner(owner);
        file.setDirectory(false);
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

    public void createNewDirectory(String fileName, String path, FileEntity parent, UserEntity owner)
    {
        FileEntity file = new FileEntity();
        file.setName(fileName);
        file.setPath(path);
        file.setOwner(owner);
        file.setDirectory(true);
        file.setParent(parent);
        fileRepository.save(file);
    }
}
