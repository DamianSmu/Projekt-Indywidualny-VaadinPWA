package com.projekt.vaadinpwa.backend.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Profile("prod")
@Service
public class S3DataSourceService implements DataSourceService {

    private static final String bucketName = "vaadin-pwa-files-bucket";

    private final AmazonS3 s3client;

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
        try {
            s3client.deleteObject(new DeleteObjectRequest(bucketName, path + name));
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }


    public byte[] downloadZip(String path) {
        String delimiter = "/";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(os);
        ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucketName).withPrefix(path);
        try {
            ObjectListing result;
            do {
                result = s3client.listObjects(request);
                for (S3ObjectSummary summary : result.getObjectSummaries()) {
                    if (!summary.getKey().endsWith(delimiter)) {
                        S3Object s3object = s3client.getObject(bucketName, summary.getKey());
                        S3ObjectInputStream inputStream = s3object.getObjectContent();
                        ZipEntry zipEntry = new ZipEntry(summary.getKey());
                        zos.putNextEntry(zipEntry);
                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = inputStream.read(bytes)) >= 0) {
                            zos.write(bytes, 0, length);
                        }
                        zos.closeEntry();
                        inputStream.close();
                    }
                }
                request.setMarker(result.getMarker());
            }
            while (result.isTruncated());
            zos.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }

}
