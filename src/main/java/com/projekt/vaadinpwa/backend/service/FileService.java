package com.projekt.vaadinpwa.backend.service;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.projekt.vaadinpwa.backend.entity.UserEntity;
import com.projekt.vaadinpwa.backend.repository.FileRepository;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final UserService userService;
    private final DataSourceService dataSourceService;

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
/*
        userService.saveUser("user2", "testuser@gmail.com", "password");
        UserEntity testUser2 = userService.findByUserName("user2").get();

        FileEntity a = new FileEntity("TEST", "hthrd", true, null, testUser);
        FileEntity b = new FileEntity("TEST", "hthrd", true, null, testUser2);
        FileEntity c = new FileEntity("TEST", "hthrd", false, a, testUser);
        FileEntity d = new FileEntity("TEST", "hthrd", false, b, testUser);
        FileEntity e = new FileEntity("TEST", "hthrd", false, a, testUser2);
        FileEntity f = new FileEntity("TEST", "hthrd", false, b, testUser2);
        fileRepository.save(a);
        fileRepository.save(b);
        fileRepository.save(c);
        fileRepository.save(d);
        fileRepository.save(e);
        fileRepository.save(f);*/
    }

    public byte[] downloadFile(String path, String name) {
        return dataSourceService.downloadFile(path, name);
    }

    public byte[] downloadZip(String path) {
        return dataSourceService.downloadZip(path);
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
        if (directory != null)
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

    public void deleteFile(FileEntity fileEntity) {
        while (!getChildren(fileEntity).isEmpty())
            getChildren(fileEntity).forEach(this::deleteFile);
        dataSourceService.removeFile(fileEntity.getName(), fileEntity.getPath());
        fileRepository.delete(fileEntity);
    }

    public TreeData<FileEntity> getAllOwnersFilesTreeData(UserEntity owner) {
        List<FileEntity> files = fileRepository.findByOwner(owner);
        Set<FileEntity> fileSet = new HashSet<>();
        TreeData<FileEntity> treeData = new TreeData<>();
        for (FileEntity file : files) {
            fileSet.add(file);
            FileEntity currentFile = file;
            while (currentFile.getParent().isPresent()) {
                fileSet.add(currentFile.getParent().get());
                currentFile = currentFile.getParent().get();
            }
        }
        treeData.addRootItems(fileSet.stream().filter(f -> !f.getParent().isPresent()));
        fileSet.removeIf(f -> !f.getParent().isPresent());
        while (!fileSet.isEmpty()) {
            fileSet.stream().filter(f -> treeData.contains(f.getParent().get())).forEach(f -> treeData.addItem(f.getParent().get(), f));
            fileSet.removeIf(f -> treeData.contains(f.getParent().get()));
        }
        return treeData;
    }
}
