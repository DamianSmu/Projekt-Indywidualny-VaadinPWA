package com.projekt.vaadinpwa.backend.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Optional;

@Entity
public class FileEntity extends AbstractEntity {

    private String name;
    private String path;
    private boolean directory;

    @ManyToOne(optional = true)
    private FileEntity parent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity owner;

    public FileEntity() {}

    public FileEntity(String name, String path, boolean directory, FileEntity parent, UserEntity owner) {
        this.name = name;
        this.path = path;
        this.directory = directory;
        this.parent = parent;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public Optional<FileEntity> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(FileEntity parent) {
        this.parent = parent;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }
}
