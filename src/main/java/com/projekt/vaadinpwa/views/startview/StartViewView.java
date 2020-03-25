package com.projekt.vaadinpwa.views.startview;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.projekt.vaadinpwa.views.main.MainView;

@Route(value = "start", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("StartView")
@CssImport("styles/views/startview/start-view-view.css")
public class StartViewView extends Div {

    public StartViewView() {
        setId("start-view-view");
        add(new Label("Content placeholder"));
    }

}
