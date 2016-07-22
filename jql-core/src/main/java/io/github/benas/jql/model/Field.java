/**
 * The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package io.github.benas.jql.model;

public class Field {

    private int id, typeId;

    private String name, type;

    private boolean isPublic, isStatic, isFinal, isTransient;

    public Field(String name, String type, boolean isPublic, boolean isStatic, boolean isFinal, boolean isTransient, int typeId) {
        this.name = name;
        this.type = type;
        this.isPublic = isPublic;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isTransient = isTransient;
        this.typeId = typeId;
    }

    public Field(int id, String name, String type, boolean isPublic, boolean isStatic, boolean isFinal, boolean isTransient, int typeId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isPublic = isPublic;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isTransient = isTransient;
        this.typeId = typeId;
    }

    public int getId() {
        return id;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isTransient() {
        return isTransient;
    }
}
