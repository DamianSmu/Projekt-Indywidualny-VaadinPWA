package com.projekt.vaadinpwa.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class StartView extends VerticalLayout {
    public StartView()
    {
        add(new H1("TEST"));
    }
}
