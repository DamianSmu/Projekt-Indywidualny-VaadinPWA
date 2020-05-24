package com.projekt.vaadinpwa.views.components;

import com.projekt.vaadinpwa.backend.entity.FileEntity;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.treegrid.TreeGrid;

public class FileGrid {
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
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setDetailsVisibleOnClick(false);
        return grid;
    }
}
