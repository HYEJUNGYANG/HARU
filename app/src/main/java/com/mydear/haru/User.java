package com.mydear.haru;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String type;

    public User() {
        this.id = null;
        this.name = null;
        this.type = null;
    }

    public User(String id, String name, String type) {
        setId(id);
        setName(name);
        setType(type);
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
