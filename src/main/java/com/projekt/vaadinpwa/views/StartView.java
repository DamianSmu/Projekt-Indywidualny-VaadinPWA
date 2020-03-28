package com.projekt.vaadinpwa.views;

import com.projekt.vaadinpwa.backend.service.S3ConnectionService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

@Route("")
public class StartView extends VerticalLayout {

    private S3ConnectionService s3ConnectionService;

    public StartView(S3ConnectionService s3ConnectionService) {
        this.s3ConnectionService = s3ConnectionService;

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        add(new H1("Dodaj plik"));
        configureUploader();
    }

    private void configureUploader() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(event -> {
            s3ConnectionService.uploadFile("", event.getFileName(), buffer.getInputStream(), event.getContentLength());
            add(new H2("Plik" + event.getFileName() + " dodany pomyślnie"));
        });
        add(upload);
    }
}
