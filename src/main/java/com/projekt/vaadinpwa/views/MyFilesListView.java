package com.projekt.vaadinpwa.views;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.projekt.vaadinpwa.backend.entity.UserEntity;
import com.projekt.vaadinpwa.backend.service.FileService;
import com.projekt.vaadinpwa.backend.service.UserService;
import com.projekt.vaadinpwa.views.components.DownloadButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "myfiles", layout = MainLayout.class)
@PageTitle("Moje pliki | ShareYourNotes")
public class MyFilesListView extends VerticalLayout {

    private TreeGrid<FileEntity> grid = new TreeGrid<>(FileEntity.class);
    private FileService fileService;
    private UserEntity loggedUser;

    public MyFilesListView(FileService fileService, UserService userService) {
        addClassName("list-file");
        this.fileService = fileService;

        loggedUser = userService.getLoggedUser().get();

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        configureGrid();
        add(new H1("Twoje pliki"), grid, createNavigationButtons());
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
        grid.setDataProvider(new TreeDataProvider<>(fileService.getAllOwnersFilesTreeData(loggedUser)));
        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(f -> createItemDetails(f, f.getOwner().equals(loggedUser))));
    }

    private Button createDeleteButton(FileEntity file) {
        Button button = new Button("Usuń");
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        ConfirmDialog dialog = new ConfirmDialog("Potwierdzenie usunięcia",
                "Czy na pewno chcesz usunąć?",
                "Usuń", e -> {
            fileService.deleteFile(file);
            grid.setDataProvider(new TreeDataProvider<>(fileService.getAllOwnersFilesTreeData(loggedUser)));
            grid.getDataProvider().refreshAll();
        }, "Anuluj", e -> {
        });
        dialog.setConfirmButtonTheme("error primary");
        button.addClickListener(event -> dialog.open());
        return button;
    }

    private Button createDetailsButton(FileEntity file) {
        Button button = new Button();
        button.setIcon(VaadinIcon.ELLIPSIS_DOTS_H.create());
        button.addClickListener(e -> grid.setDetailsVisible(file, !grid.isDetailsVisible(file)));
        return button;
    }

    private HorizontalLayout createItemDetails(FileEntity item, boolean deleteAccess) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("layout-with-border");
        layout.setJustifyContentMode(JustifyContentMode.START);
        layout.setAlignItems(Alignment.CENTER);
        layout.setMaxWidth("50em");
        layout.add(DownloadButton.create(item, fileService));
        if (deleteAccess) {
            layout.add(createDeleteButton(item));
        }
        Label date = new Label("Data dodania: 05-05-2020");
        layout.add(date);
        layout.expand(date);
        return layout;
    }

    public Component createNavigationButtons()
    {
        Button goToUploadButton = new Button("Dodaj plik");
        goToUploadButton.addClickListener(e -> UI.getCurrent().navigate(UploadFileView.class));
        goToUploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button goToListFileButton = new Button("Zobacz pliki");
        goToListFileButton.addClickListener(e -> UI.getCurrent().navigate(ListFileView.class));
        goToListFileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return new HorizontalLayout(goToUploadButton, goToListFileButton);
    }
}
