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
package io.github.benas.jcql.model;

public class Method {

    private int id;
    private String name;
    private boolean isPublic;
    private boolean isStatic;
    private boolean isFinal;
    private boolean isAbstract;
    private boolean isConstructor;
    private int typeId;

    public Method(String name, boolean isPublic, boolean isStatic, boolean isFinal, boolean isAbstract, boolean isConstructor, int typeId) {
        this.name = name;
        this.isPublic = isPublic;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
        this.isConstructor = isConstructor;
        this.typeId = typeId;
    }

    public Method(int id, String name, boolean isPublic, boolean isStatic, boolean isFinal, boolean isAbstract, boolean isConstructor, int typeId) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
        this.isConstructor = isConstructor;
        this.typeId = typeId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTypeId() {
        return typeId;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isConstructor() {
        return isConstructor;
    }
}
