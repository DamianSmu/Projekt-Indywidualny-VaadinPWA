package com.projekt.vaadinpwa.views;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.projekt.vaadinpwa.backend.service.FileService;
import com.projekt.vaadinpwa.views.components.DownloadButton;
import com.projekt.vaadinpwa.views.components.FileGrid;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "list", layout = MainLayout.class)
@PageTitle("Wszystkie pliki | ShareYourNotes")
public class ListFileView extends VerticalLayout {

    private TreeGrid<FileEntity> grid;
    private FileService fileService;

    public ListFileView(FileService fileService) {
        addClassName("list-file");
        this.fileService = fileService;

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        grid = FileGrid.createBasicFileGrid();
        configureGrid();
        add(new H1("Wszystkie pliki"), grid, createNavigationButtons());
    }

    private void configureGrid() {
        grid.setItems(fileService.getRoot(), fileService::getChildren);
        grid.addComponentColumn(this::createDetailsButton);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::createItemDetails));
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
        layout.add(DownloadButton.create(item, fileService));
        return layout;
    }

    private Component createNavigationButtons()
    {
        Button goToUploadButton = new Button("Dodaj plik");
        goToUploadButton.addClickListener(e -> UI.getCurrent().navigate(UploadFileView.class));
        goToUploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button goToStartButton = new Button("Wróć");
        goToStartButton.addClickListener(e -> UI.getCurrent().navigate(StartView.class));

        return new HorizontalLayout(goToUploadButton, goToStartButton);
    }
}
