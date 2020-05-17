package com.projekt.vaadinpwa.backend.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Profile("!prod")
@Service
public class LocalDataSourceService implements DataSourceService {
    @Override
    public void uploadFile(String name, String path, InputStream stream, Long streamLength) {
        new File(path).mkdirs();
        String strNewFile = path + name;
        File newFile = new File(strNewFile);
        Path newFilePath = newFile.toPath();
        try {
            Files.copy(stream, newFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] downloadFile(String path, String name) {
        try {
            return Files.readAllBytes(Paths.get(path + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public void removeFile(String name, String path) {
        new File(path + name).delete();
    }

    @Override
    public byte[] downloadZip(String path) {
        //TODO
        return new byte[0];
    }
}
