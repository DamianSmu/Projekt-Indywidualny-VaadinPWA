package com.projekt.vaadinpwa.backend.service;

import java.io.InputStream;

public interface DataSourceService {
    void uploadFile(String name, String path, InputStream stream, Long streamLength) throws Exception;

    byte[] downloadFile(String path, String name);

    void removeFile(String name, String path);
}
