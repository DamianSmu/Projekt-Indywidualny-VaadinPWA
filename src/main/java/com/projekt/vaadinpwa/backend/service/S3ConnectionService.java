package com.projekt.vaadinpwa.backend.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class S3ConnectionService {

    private static final String accessKey = "AKIAIVSJQDBDI7UQMIOA";
    private static final String privateKey = "MWW9osFrj5IljeWKiryBXIKe95MzcMd4fGL7oZMt";
    private static final String bucketName = "vaadin-pwa-files-bucket";

    private AmazonS3 s3client;

    public S3ConnectionService() {
        init();
    }


    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, privateKey);
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    public void uploadFile(String path, String name, InputStream stream, Long streamLength) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(streamLength);
        s3client.putObject(
                bucketName,
                path + name,
                stream,
                objectMetadata
        );
    }

    public void downloadFile(String path, String name) {
        S3Object s3object = s3client.getObject(bucketName, path + name);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
