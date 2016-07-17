package io.github.benas.jcql.model;

public class Extends {

    private int typeId;

    private int extendedTypeId;

    public Extends(int typeId, int extendedTypeId) {
        this.typeId = typeId;
        this.extendedTypeId = extendedTypeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getExtendedTypeId() {
        return extendedTypeId;
    }
}
