package com.projekt.vaadinpwa.views.translation;

import com.vaadin.flow.component.upload.UploadI18N;

public class UploadTranslation {
    public static UploadI18N get()
    {
        UploadI18N i18n = new UploadI18N();
        i18n.setDropFiles(
                new UploadI18N.DropFiles().setOne("Wybierz plik..."))
                .setAddFiles(new UploadI18N.AddFiles()
                        .setOne("Wybierz"))
                .setCancel("Anuluj")
                .setError(new UploadI18N.Error()
                        .setTooManyFiles("Zbyt wiele plików.")
                        .setFileIsTooBig("Wybrany plik może mieć rozmiar maksymalnie 10MB")
                        .setIncorrectFileType("Nieprawidłowy typ pliku."))
                .setUploading(new UploadI18N.Uploading()
                        .setStatus(new UploadI18N.Uploading.Status()
                                .setConnecting("Łączenie...")
                                .setProcessing("Dodawanie..."))
                        .setError(new UploadI18N.Uploading.Error()
                                .setServerUnavailable("Serwer niedostępny.")
                                .setUnexpectedServerError("Niespodziewany błąd")
                                .setForbidden("Dostęp zabroniony")));
        return i18n;
    }
}
