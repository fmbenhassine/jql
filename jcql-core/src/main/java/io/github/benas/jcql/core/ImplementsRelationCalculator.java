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
package io.github.benas.jcql.core;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.github.benas.jcql.domain.ImplementsDao;
import io.github.benas.jcql.domain.TypeDao;
import io.github.benas.jcql.model.Implements;

import java.util.List;

public class ImplementsRelationCalculator {

    private TypeDao typeDao;

    private ImplementsDao implementsDao;

    public ImplementsRelationCalculator(TypeDao typeDao, ImplementsDao implementsDao) {
        this.typeDao = typeDao;
        this.implementsDao = implementsDao;
    }

    public void calculate(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, CompilationUnit compilationUnit) {
        List<ClassOrInterfaceType> implementedInterfaces = classOrInterfaceDeclaration.getImplements();
        for (ClassOrInterfaceType implementedInterface : implementedInterfaces) {
            String implementedInterfaceName = implementedInterface.getName();
            String implementedInterfacePackageName = ((CompilationUnit) implementedInterface.getParentNode().getParentNode()).getPackage().getPackageName();
            if (typeDao.exist(implementedInterfaceName, implementedInterfacePackageName)) { // JDK interfaces are not indexed
                int interfaceId = typeDao.getId(implementedInterfaceName, implementedInterfacePackageName);
                int nbClasses = typeDao.count(classOrInterfaceDeclaration.getName(), compilationUnit.getPackage().getPackageName());
                if (nbClasses > 1) {
                    System.err.println("More than one class having the same name '" + classOrInterfaceDeclaration.getName() + "' and package '" + compilationUnit.getPackage().getPackageName() + "'");
                } else {
                    int classId = typeDao.getId(classOrInterfaceDeclaration.getName(), compilationUnit.getPackage().getPackageName());
                    implementsDao.save(new Implements(classId, interfaceId));
                }
            }
        }
    }
}
