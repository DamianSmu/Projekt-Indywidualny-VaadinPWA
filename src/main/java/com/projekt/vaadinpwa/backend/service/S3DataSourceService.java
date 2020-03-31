package com.projekt.vaadinpwa.backend.service;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Profile("prod")
@Service
public class S3DataSourceService implements DataSourceService {

    private static final String bucketName = "vaadin-pwa-files-bucket";

    private AmazonS3 s3client;

    public S3DataSourceService() {
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    @Override
    public void uploadFile(String name, String path, InputStream stream, Long streamLength) throws Exception {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(streamLength);
            s3client.putObject(
                    bucketName,
                    path + name,
                    stream,
                    objectMetadata
            );
        } catch (Exception e) {
            throw new Exception("Cannot upload file");
        }
    }

    @Override
    public byte[] downloadFile(String path, String name) {
        byte[] bytes = new byte[0];
        S3Object s3object = s3client.getObject(bucketName, path + name);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public void removeFile(String name, String path) {

    }
}
