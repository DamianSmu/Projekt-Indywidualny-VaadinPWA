package com.projekt.vaadinpwa.backend.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
public class UserEntity extends AbstractEntity {

    @NotNull
    @NotEmpty
    private String userName = "";

    @Email
    @NotNull
    @NotEmpty
    private String email = "";

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private final List<FileEntity> files = new LinkedList<>();

    @NotNull
    private String password = "";

    public UserEntity() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<FileEntity> getFiles() {
        return files;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
