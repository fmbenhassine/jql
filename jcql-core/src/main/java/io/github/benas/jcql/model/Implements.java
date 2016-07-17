package io.github.benas.jcql.model;

public class Implements {

    private int classId;

    private int interfaceId;

    public Implements(int classId, int interfaceId) {
        this.classId = classId;
        this.interfaceId = interfaceId;
    }

    public int getClassId() {
        return classId;
    }

    public int getInterfaceId() {
        return interfaceId;
    }
}
