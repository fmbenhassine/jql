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

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.github.benas.jql.domain.ExtendsDao;
import io.github.benas.jql.domain.TypeDao;
import io.github.benas.jql.model.Extends;

import java.util.List;

public class ExtendsRelationCalculator {

    private TypeDao typeDao;

    private ExtendsDao extendsDao;

    public ExtendsRelationCalculator(TypeDao typeDao, ExtendsDao extendsDao) {
        this.typeDao = typeDao;
        this.extendsDao = extendsDao;
    }

    public void calculate(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, CompilationUnit compilationUnit) {
        List<ClassOrInterfaceType> extendedTypes = classOrInterfaceDeclaration.getExtends();
        for (ClassOrInterfaceType extendedType : extendedTypes) {
            String extendedTypeName = extendedType.getName();
            String extendedTypePackageName = ((CompilationUnit) extendedType.getParentNode().getParentNode()).getPackage().getPackageName();
            if (typeDao.exist(extendedTypeName, extendedTypePackageName)) { // JDK interfaces are not indexed
                int extendedInterfaceId = typeDao.getId(extendedTypeName, extendedTypePackageName);
                int interfaceId = typeDao.getId(classOrInterfaceDeclaration.getName(), compilationUnit.getPackage().getPackageName());
                extendsDao.save(new Extends(interfaceId, extendedInterfaceId));
            }
        }
    }
}
