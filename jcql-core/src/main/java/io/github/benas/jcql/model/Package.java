package io.github.benas.jcql.model;

public class Package {

    private int id;
    private String name;

    public Package(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
