package com.projekt.vaadinpwa.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Start | VaadinPWA")
public class StartView extends VerticalLayout {

    public StartView() {
        Button goToUploadButton = new Button("Dodaj plik");
        goToUploadButton.addClickListener(e -> UI.getCurrent().navigate(UploadFileView.class));
        goToUploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button goToListFileButton = new Button("Zobacz pliki");
        goToListFileButton.addClickListener(e -> UI.getCurrent().navigate(ListFileView.class));
        goToListFileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        add(new H2("Wybierz chcesz zrobić"), new HorizontalLayout(goToUploadButton, goToListFileButton));
    }
}
