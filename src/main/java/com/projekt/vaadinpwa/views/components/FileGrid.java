package com.projekt.vaadinpwa.views.components;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.treegrid.TreeGrid;

import java.text.SimpleDateFormat;

public class FileGrid {
    private static final String datePattern = "dd-MM-yyyy HH:mm";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

    public static TreeGrid<FileEntity> createBasicFileGrid()
    {
        TreeGrid<FileEntity> grid = new TreeGrid<>(FileEntity.class);
        grid.removeAllColumns();
        grid.addComponentColumn(file ->
                file.isDirectory() ? VaadinIcon.FOLDER_OPEN_O.create() : VaadinIcon.FILE_O.create()
        ).setFlexGrow(0).setWidth("100px");
        grid.addHierarchyColumn(FileEntity::getName).setHeader("Nazwa");
        grid.addColumn(file ->
                file.getOwner() == null ? "-" : file.getOwner().getUserName()
        ).setHeader("Właściciel");
        grid.addColumn(f -> simpleDateFormat.format(f.getCreatedDate())).setHeader("Data utworzenia");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setDetailsVisibleOnClick(false);
        return grid;
    }
}
