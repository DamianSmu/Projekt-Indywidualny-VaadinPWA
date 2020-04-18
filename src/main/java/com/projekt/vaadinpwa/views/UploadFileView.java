package com.projekt.vaadinpwa.views;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.projekt.vaadinpwa.backend.entity.UserEntity;
import com.projekt.vaadinpwa.backend.service.FileService;
import com.projekt.vaadinpwa.backend.service.UserService;
import com.projekt.vaadinpwa.views.translation.UploadTranslation;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "upload", layout = MainLayout.class)
@PageTitle("Dodaj plik | ShareYourNotes")
public class UploadFileView extends VerticalLayout {

    private final FileService fileService;
    private final UserService userService;
    private String selectedPath = "";
    private FileEntity selectedDirectory = null;

    private Upload upload = new Upload();
    private TreeGrid<FileEntity> folderGrid;
    private Button newFolderButton = new Button();
    private TextField newFolderName = new TextField();
    private H3 chosenFolderLabel;


    private UserEntity loggedUser;

    public UploadFileView(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;

        loggedUser = userService.getLoggedUser().get();

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        configureUploader();
        configureGrid();
        configureNewFolderButtonAndTextField();

        Button goToListFileButton = new Button("Zobacz pliki");
        goToListFileButton.addClickListener(e -> UI.getCurrent().navigate(ListFileView.class));
        goToListFileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button goToStartButton = new Button("Wróć");
        goToStartButton.addClickListener(e -> UI.getCurrent().navigate(StartView.class));

        HorizontalLayout newFileLayout = new HorizontalLayout();
        newFileLayout.setWidthFull();
        newFileLayout.setAlignItems(Alignment.START);
        newFileLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        newFileLayout.add(
                chosenFolderLabel = new H3("Wybrany folder to: " + (selectedPath.equals("") ? "katalog główny" : selectedPath)),
                newFolderName,
                newFolderButton
        );
        newFileLayout.expand(chosenFolderLabel);

        add(
                new H1("Dodaj plik"),
                upload,
                new HorizontalLayout(goToListFileButton, goToStartButton),
                newFileLayout,
                folderGrid
        );
    }

    private void configureUploader() {
        MemoryBuffer buffer = new MemoryBuffer();
        upload.setReceiver(buffer);
        upload.setI18n(UploadTranslation.get());
        upload.addSucceededListener(event -> uploadFile(event, buffer));
        upload.setMaxFileSize(10000000);
        add(upload);
    }

    private TreeGrid<FileEntity> configureGrid() {
        folderGrid = new TreeGrid<>(FileEntity.class);
        folderGrid.removeAllColumns();
        folderGrid.addComponentColumn(file -> VaadinIcon.FOLDER_OPEN_O.create()).setFlexGrow(0).setWidth("100px");
        folderGrid.addHierarchyColumn(FileEntity::getName).setHeader("Nazwa");
        folderGrid.addColumn(file ->
                file.getOwner() == null ? "-" : file.getOwner().getUserName()
        ).setHeader("Właściciel");
        folderGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        folderGrid.asSingleSelect().addValueChangeListener(e -> {
            selectedPath = e.getValue() == null ? "" : e.getValue().getPath();
            selectedDirectory = e.getValue();
            chosenFolderLabel.setText("Wybrany folder to: " + (selectedPath.equals("") ? "katalog główny" : selectedPath));
        });
        folderGrid.setItems(fileService.getDirRoot(), fileService::getDirChildren);
        return folderGrid;
    }

    private void configureNewFolderButtonAndTextField() {
        newFolderName.setPlaceholder("Nazwa nowego folderu...");
        newFolderName.setClearButtonVisible(true);
        newFolderName.setValueChangeMode(ValueChangeMode.LAZY);
        newFolderName.addValueChangeListener(e -> {
            if (newFolderName.getValue().length() < 3) {
                newFolderName.setErrorMessage("Nazwa folderu musi mieć conajmniej 3 znaki.");
                newFolderName.setInvalid(true);
                newFolderButton.setEnabled(false);
            } else if (!newFolderName.getValue().matches("^[a-zA-Z0-9_-]*$")) {
                newFolderName.setErrorMessage("Nazwa folderu nie może zawierać białych znaków oraz znaków specjalnych oprócz: - _");
                newFolderName.setInvalid(true);
                newFolderButton.setEnabled(false);
            } else {
                newFolderName.setInvalid(false);
                newFolderButton.setEnabled(true);
            }
        });

        newFolderButton.setText("Utwórz folder");
        newFolderButton.setEnabled(false);
        newFolderButton.setIcon(VaadinIcon.PLUS.create());
        newFolderButton.addClickListener(e -> createNewFolder());
    }

    private void createNewFolder() {
        FileEntity newFile = fileService.createNewDirectory(newFolderName.getValue(), selectedPath + newFolderName.getValue() + "/", selectedDirectory, loggedUser);
        folderGrid.setDataProvider(new TreeDataProvider<>((new TreeData<FileEntity>()).addItems(fileService.getDirRoot(), fileService::getDirChildren)));
        folderGrid.getDataProvider().refreshAll();
        if (newFile.getParent().isPresent()) {
            folderGrid.expand(newFile.getParent().get());
        }
    }

    private void uploadFile(SucceededEvent event, MemoryBuffer buffer)
    {
        fileService.uploadFile(event.getFileName(), selectedPath, buffer.getInputStream(), event.getContentLength(), loggedUser, selectedDirectory);
        Notification.show(
                "Plik " + event.getFileName() + " został dodany pomyślnie.", 4000,
                Notification.Position.BOTTOM_CENTER);
    }
}
