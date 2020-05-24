package com.projekt.vaadinpwa.views.components;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.projekt.vaadinpwa.backend.service.FileService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;

public class DownloadButton {

    public static Anchor create(FileEntity file, FileService fileService) {
        Button btnDownload;
        StreamResource streamResource;
        String fileName = file.getName();
        String filePath = file.getPath();
        if (!file.isDirectory()) {
            btnDownload = new Button("Pobierz plik");
            streamResource = getStreamResource(fileService.downloadFile(filePath, fileName), fileName);
        } else {
            btnDownload = new Button("Pobierz ZIP");
            fileName = filePath.replace("/", "-").substring(0, filePath.length() - 1).concat(".zip");
            streamResource = getStreamResource(fileService.downloadZip(filePath), fileName);
        }
        Anchor anchorDownload = new Anchor(streamResource, "");
        anchorDownload.getElement().setAttribute("download", true);
        anchorDownload.add(btnDownload);
        return anchorDownload;
    }

    private static StreamResource getStreamResource(byte[] byteArray, String name) {
        return new StreamResource(name, () -> new ByteArrayInputStream(byteArray));
    }
}
