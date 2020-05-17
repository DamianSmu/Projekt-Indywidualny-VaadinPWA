package com.projekt.vaadinpwa.views;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.projekt.vaadinpwa.backend.service.FileService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
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
        addClassName("list-file");
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
        grid.addComponentColumn(this::createDetailsButton);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(fileService.getRoot(), fileService::getChildren);

        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::createItemDetails));
    }

    public Anchor createDownloadButton(FileEntity file) {
        Button btnDownload;
        StreamResource streamResource;
        if (!file.isDirectory()) {
            btnDownload = new Button("Pobierz plik");
            streamResource = new StreamResource(file.getName(), () ->
                    new ByteArrayInputStream(fileService.downloadFile(file.getPath(), file.getName())));
        } else {
            btnDownload = new Button("Pobierz ZIP");
            String fileName = file.getPath().replace("/", "-").substring(0, file.getPath().length() - 1).concat(".zip");
            streamResource = new StreamResource(fileName, () ->
                    new ByteArrayInputStream(fileService.downloadZip(file.getPath())));
        }
        Anchor anchorDownload = new Anchor(streamResource, "");
        anchorDownload.getElement().setAttribute("download", true);
        anchorDownload.add(btnDownload);
        return anchorDownload;
    }

    public Button createDeleteButton(FileEntity file) {
        Button button = new Button("Usuń");
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        ConfirmDialog dialog = new ConfirmDialog("Potwierdzenie usunięcia",
                "Czy na pewno chcesz usunąć?",
                "Usuń", e -> {
            fileService.deleteFile(file);
            grid.setDataProvider(new TreeDataProvider<>((new TreeData<FileEntity>()).addItems(fileService.getRoot(), fileService::getChildren)));
            grid.getDataProvider().refreshAll();
        }, "Anuluj", e -> {});
        dialog.setConfirmButtonTheme("error primary");
        button.addClickListener(event -> dialog.open());
        return button;
    }

    public Button createDetailsButton(FileEntity file) {
        Button button = new Button();
        button.setIcon(VaadinIcon.ELLIPSIS_DOTS_H.create());
        button.addClickListener(e -> grid.setDetailsVisible(file, !grid.isDetailsVisible(file)));
        return button;
    }

    private HorizontalLayout createItemDetails(FileEntity item) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("layout-with-border");
        layout.setJustifyContentMode(JustifyContentMode.START);
        layout.setAlignItems(Alignment.CENTER);
        layout.setMaxWidth("50em");
        layout.add(createDownloadButton(item), createDeleteButton(item));
        Label date = new Label("Data dodania: 05-05-2020");
        layout.add(date);
        layout.expand(date);
        return layout;
    }
}
