package com.projekt.vaadinpwa.views;

import com.projekt.vaadinpwa.backend.service.FileService;
import com.projekt.vaadinpwa.views.translation.UploadTranslation;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "upload", layout = MainLayout.class)
@PageTitle("Dodaj plik | ShareYourNotes")
public class UploadFileView extends VerticalLayout {
    
    private FileService fileService;

    public UploadFileView(FileService fileService) {
        this.fileService = fileService;

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        add(new H1("Dodaj plik"));
        configureUploader();

        Button goToListFileButton = new Button("Zobacz pliki");
        goToListFileButton.addClickListener(e -> UI.getCurrent().navigate(ListFileView.class));
        goToListFileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button goToStartButton = new Button("Wróć");
        goToStartButton.addClickListener(e -> UI.getCurrent().navigate(StartView.class));

        add((new HorizontalLayout(goToListFileButton, goToStartButton)));
    }

    private void configureUploader() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setI18n(UploadTranslation.get());
        upload.addSucceededListener(event -> {
            fileService.uploadFile(event.getFileName(), /*TODO*/"", buffer.getInputStream(), event.getContentLength(), /*TODO*/null);
            add(new Paragraph("Plik " + event.getFileName() + " dodany pomyślnie."));
        });
        add(upload);
    }
}
