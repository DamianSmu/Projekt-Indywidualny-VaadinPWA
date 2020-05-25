package com.projekt.vaadinpwa.views;

import com.projekt.vaadinpwa.backend.security.SecurityUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Start | ShareYourNotes")
public class StartView extends VerticalLayout {

    public StartView() {
        String username = SecurityUtils.getLoggedUserName();
        UI.getCurrent().getPage().executeJs(
                "localStorage.setItem('username', '"+ username +", jesteś offline.')");

        Button goToUploadButton = new Button("Dodaj plik");
        goToUploadButton.addClickListener(e -> UI.getCurrent().navigate(UploadFileView.class));
        goToUploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button goToListFileButton = new Button("Zobacz wszystkie pliki");
        goToListFileButton.addClickListener(e -> UI.getCurrent().navigate(ListFileView.class));
        goToListFileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button goToMyFilesButton = new Button("Zobacz swoje pliki");
        goToMyFilesButton.addClickListener(e -> UI.getCurrent().navigate(MyFilesListView.class));
        goToMyFilesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        String userName = SecurityUtils.getLoggedUserName();

        add(new H2("Witaj " + userName + "!"), new H3("Wybierz co chcesz zrobić"), new HorizontalLayout(goToUploadButton, goToListFileButton, goToMyFilesButton));
    }
}
