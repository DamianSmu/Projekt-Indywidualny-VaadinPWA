package com.projekt.vaadinpwa.views;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.projekt.vaadinpwa.backend.service.FileService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;

@Route(value = "list", layout = MainLayout.class)
@PageTitle("Wszystkie pliki | ShareYourNotes")
public class ListFileView extends VerticalLayout {

    private TreeGrid<FileEntity> grid = new TreeGrid<>(FileEntity.class);
    private FileService fileService;

    public ListFileView(FileService fileService) {

        this.fileService = fileService;

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        Button goToUploadButton = new Button("Dodaj plik");
        goToUploadButton.addClickListener(e -> UI.getCurrent().navigate(UploadFileView.class));
        goToUploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button goToStartButton = new Button("Wróć");
        goToStartButton.addClickListener(e -> UI.getCurrent().navigate(StartView.class));

        configureGrid();
        add(new H1("Wszystkie pliki"), grid, (new HorizontalLayout(goToUploadButton, goToStartButton)));
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addComponentColumn(file ->
                file.isDirectory() ? VaadinIcon.FOLDER_OPEN_O.create() : VaadinIcon.FILE_O.create()
        ).setFlexGrow(0).setWidth("100px");
        grid.addHierarchyColumn(FileEntity::getName).setHeader("Nazwa");
        grid.addColumn(file ->
                file.getOwner() == null ? "-" : file.getOwner().getUserName()
        ).setHeader("Właściciel");
        grid.addComponentColumn(this::createDownloadButton);
        grid.setItems(fileService.findAll());
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(fileService.getRoot(), fileService::getChildren);
    }

    public Anchor createDownloadButton(FileEntity file) {
        Anchor anchorDownload;
        if (!file.isDirectory()) {
            Button btnDownload = new Button("Pobierz");
            StreamResource StreamResource = new StreamResource(file.getName(), () ->
                    new ByteArrayInputStream(fileService.downloadFile(file.getPath(), file.getName())));
            anchorDownload = new Anchor(StreamResource, "");
            anchorDownload.getElement().setAttribute("download", true);
            anchorDownload.add(btnDownload);
            return anchorDownload;
        }
        anchorDownload = new Anchor();
        anchorDownload.setVisible(false);
        return anchorDownload;
    }
}
