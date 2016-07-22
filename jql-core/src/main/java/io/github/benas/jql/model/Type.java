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

public class Type {

    protected int id;
    protected String name;
    protected boolean isPublic;
    protected boolean isStatic;
    protected boolean isFinal;
    protected boolean isAbstract;
    protected boolean isClass;
    protected boolean isInterface;
    protected boolean isEnumeration;
    protected boolean isAnnotation;
    protected int compilationUnitId;

    public Type(String name, boolean isPublic, boolean isStatic, boolean isFinal, boolean isAbstract, boolean isClass, boolean isInterface, boolean isEnumeration, boolean isAnnotation, int compilationUnitId) {
        this.name = name;
        this.isPublic = isPublic;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
        this.isClass = isClass;
        this.isInterface = isInterface;
        this.isEnumeration = isEnumeration;
        this.isAnnotation = isAnnotation;
        this.compilationUnitId = compilationUnitId;
    }

    public Type(int id, String name, boolean isPublic, boolean isStatic, boolean isFinal, boolean isAbstract, boolean isClass, boolean isInterface, boolean isEnumeration, boolean isAnnotation, int compilationUnitId) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
        this.isClass = isClass;
        this.isInterface = isInterface;
        this.isEnumeration = isEnumeration;
        this.isAnnotation = isAnnotation;
        this.compilationUnitId = compilationUnitId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompilationUnitId() {
        return compilationUnitId;
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

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isClass() {
        return isClass;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public boolean isEnumeration() {
        return isEnumeration;
    }

    public boolean isAnnotation() {
        return isAnnotation;
    }
}
