package com.projekt.vaadinpwa.views.translation;

import com.vaadin.flow.component.login.LoginI18n;

public class LoginFormTranslation {
    public static LoginI18n get() {
        final LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("ShareYourNotes");
        i18n.getForm().setTitle("Zaloguj się");
        i18n.getForm().setUsername("Nazwa użytkownika");
        i18n.getForm().setPassword("Hasło");
        i18n.getForm().setSubmit("Zaloguj");
        i18n.getForm().setForgotPassword("Nie pamiętasz hasła?");
        i18n.getErrorMessage().setTitle("Nazwa lub hasło niepoprawne.");
        i18n.getErrorMessage().setMessage("Wprowadzona nazwa użytkownika lub hasło są niepoprawne. Spróbuj ponownie.");
        i18n.setAdditionalInformation("Dev: Aby zalogować się na konto użytkownika testowego wpisz: user / password");
        return i18n;
    }
}
