package com.projekt.vaadinpwa.views;

import com.projekt.vaadinpwa.backend.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("register")
@PageTitle("Zarejestruj się | ShareYourNotes")
public class RegisterView extends VerticalLayout {
    private UserService userService;

    private TextField userName = new TextField("Nazwa użytkownika");
    private TextField email = new TextField("Adres e-mail");
    private PasswordField password = new PasswordField("Hasło");
    private Button registerButton = new Button("Zarejestruj");

    public RegisterView(UserService userService) {
        this.userService = userService;

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        setHeightFull();

        VerticalLayout fields = new VerticalLayout(userName, email, password);
        fields.setMaxWidth("30em");
        fields.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);

        email.setValueChangeMode(ValueChangeMode.LAZY);
        email.addValueChangeListener(e -> validateEmail());

        userName.setValueChangeMode(ValueChangeMode.LAZY);
        userName.addValueChangeListener(e -> validateUserName());

        password.setValueChangeMode(ValueChangeMode.LAZY);
        password.addValueChangeListener(e -> validatePassword());

        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.setEnabled(false);
        registerButton.addClickListener(e -> registerUser());

        H1 title = new H1("Zarejestruj się");
        add(title, fields, registerButton);
    }

    private void registerUser() {
        userService.saveUser(userName.getValue(), email.getValue(), password.getValue());
    }

    private void validateUserName() {
        if (userName.getValue().length() < 3) {
            userName.setErrorMessage("Nazwa użytkownika musi zawierać co najmniej 3 znaki.");
            userName.setInvalid(true);
        } else if (userService.findByUserName(userName.getValue()).isPresent()) {
            userName.setErrorMessage("Podana nazwa użytkownika już istnieje.");
            userName.setInvalid(true);
        } else {
            userName.setInvalid(false);
        }
        allowRegister();
    }

    private void validateEmail() {
        if (!email.getValue().matches("[^@]+@[^\\.]+\\..+")) {
            email.setErrorMessage("Podany e-mail jest niepoprawny");
            email.setInvalid(true);
        } else if (userService.findByUserEmail(email.getValue()).isPresent()) {
            email.setErrorMessage("Użytkownik o podanym adresie e-mail już istnieje.");
            email.setInvalid(true);
        } else {
            email.setInvalid(false);
        }
        allowRegister();
    }


    private void validatePassword() {
        if (!password.getValue().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            password.setErrorMessage("Hasło musi zawierać conajmniej 8 znaków, w tym jedną literę i cyfrę.");
            password.setInvalid(true);
        } else {
            password.setInvalid(false);
        }
        allowRegister();
    }

    private void allowRegister() {
        registerButton.setEnabled(!(userName.isInvalid() || email.isInvalid() || password.isInvalid()
                || userName.isEmpty() || email.isEmpty() || password.isEmpty()));
    }
}
