package com.projekt.vaadinpwa.backend.service;

import com.projekt.vaadinpwa.backend.entity.File;
import com.projekt.vaadinpwa.backend.entity.User;
import com.projekt.vaadinpwa.backend.repository.FileRepository;
import com.projekt.vaadinpwa.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {

    private FileRepository fileRepository;
    private UserRepository userRepository;

    public FileService(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    public List<File> findAll() {
        return fileRepository.findAll();
    }

    @PostConstruct
    public void generateTestData()
    {
        if(fileRepository.count() == 0 && userRepository.count() == 0)
        {
            generateTestUsers();
            generateTestFiles();
        }
    }

    public void generateTestUsers()
    {
        User userA = new User();
        userA.setUserName("UserA");
        userA.setEmail("usera@mail.com");
        User userB = new User();
        userB.setUserName("UserB");
        userB.setEmail("userb@mail.com");
        User userC = new User();
        userC.setUserName("UserC");
        userC.setEmail("userc@mail.com");
        List<User> users = Arrays.asList(userA, userB, userC);
        userRepository.saveAll(users);
    }

    public void generateTestFiles()
    {
        List<User> users = userRepository.findAll();
        File fileA = new File();
        fileA.setOwner(users.get(0));
        fileA.setName("fileA");
        fileA.setPath("/");
        fileA.setUrl("https://");
        File fileB = new File();
        fileB.setOwner(users.get(1));
        fileB.setName("fileB");
        fileB.setPath("/");
        fileB.setUrl("https://");
        File fileC = new File();
        fileC.setOwner(users.get(2));
        fileC.setName("fileC");
        fileC.setPath("/");
        fileC.setUrl("https://");
        List<File> files = Arrays.asList(fileA, fileB, fileC);
        fileRepository.saveAll(files);
    }
}
