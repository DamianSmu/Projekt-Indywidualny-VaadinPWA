package com.projekt.vaadinpwa.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

import java.util.HashMap;
import java.util.Map;

@PWA(name = "ShareYourNotes!", shortName = "ShareYourNotes!",
        offlinePath = "offline.html",
        offlineResources = {"offline.js"})
@CssImport("./styles/app-style.css")
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private Tabs tabs = new Tabs();
    private Map<Class<? extends Component>, Tab> navigationTargetToTab = new HashMap<>();

    public MainLayout() {
        createHeader();
        createTabs();
    }

    private void createHeader() {
        H1 logo = new H1("ShareYourNotes!");
        logo.addClassName("logo");

        Anchor logout = new Anchor("/logout", "Wyloguj");
        Image image = new Image("/icons/icon.png", "Logo");
        image.setHeight("28px");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), image, logo, logout);
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");
        addToNavbar(header);
    }

    private void createTabs() {
        addMenuTab("Dodaj pliki", UploadFileView.class);
        addMenuTab("Zobacz pliki", ListFileView.class);
        addMenuTab("Twoje pliki", MyFilesListView.class);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
        setDrawerOpened(false);
    }

    private void addMenuTab(String label, Class<? extends Component> target) {
        Tab tab = new Tab(new RouterLink(label, target));
        navigationTargetToTab.put(target, tab);
        tabs.add(tab);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        tabs.setSelectedTab(navigationTargetToTab.get(event.getNavigationTarget()));
    }
}
