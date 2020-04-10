package com.projekt.vaadinpwa.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

@CssImport("./styles/app-style.css")
public class MainLayout extends AppLayout {
    public MainLayout(){
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("ShareYourNotes");
        logo.addClassName("logo");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");
        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink uploadFileLink = new RouterLink("Dodaj pliki", UploadFileView.class);
        uploadFileLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink listFilesLink = new RouterLink("Zobacz pliki", ListFileView.class);
        uploadFileLink.setHighlightCondition(HighlightConditions.sameLocation());
        addToDrawer(new VerticalLayout(uploadFileLink, listFilesLink));
    }
}
