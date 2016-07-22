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
package io.github.benas.jql.core;

import com.github.javaparser.ast.body.*;

public class BodyDeclarationIndexer {

    private FieldIndexer fieldIndexer;
    private ConstructorIndexer constructorIndexer;
    private MethodIndexer methodIndexer;
    private AnnotationMemberIndexer annotationMemberIndexer;

    public BodyDeclarationIndexer(FieldIndexer fieldIndexer, ConstructorIndexer constructorIndexer, MethodIndexer methodIndexer, AnnotationMemberIndexer annotationMemberIndexer) {
        this.fieldIndexer = fieldIndexer;
        this.constructorIndexer = constructorIndexer;
        this.methodIndexer = methodIndexer;
        this.annotationMemberIndexer = annotationMemberIndexer;
    }

    public void index(BodyDeclaration bodyDeclaration, int typeId) {
        boolean isField = bodyDeclaration instanceof FieldDeclaration;
        boolean isMethod = bodyDeclaration instanceof MethodDeclaration;
        boolean isConstructor = bodyDeclaration instanceof ConstructorDeclaration;
        boolean isAnnotationMember = bodyDeclaration instanceof AnnotationMemberDeclaration;
        if (isField) {
            fieldIndexer.index((FieldDeclaration) bodyDeclaration, typeId);
        }
        if (isConstructor) {
            constructorIndexer.index((ConstructorDeclaration) bodyDeclaration, typeId);
        }
        if (isMethod) {
            methodIndexer.index((MethodDeclaration) bodyDeclaration, typeId);
        }
        if (isAnnotationMember) {
            annotationMemberIndexer.index((AnnotationMemberDeclaration) bodyDeclaration, typeId);
        }
    }
}
