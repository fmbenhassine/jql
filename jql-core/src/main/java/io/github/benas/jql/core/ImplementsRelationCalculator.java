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
import io.github.benas.jql.domain.ImplementsDao;
import io.github.benas.jql.domain.TypeDao;
import io.github.benas.jql.model.Implements;

import java.util.List;
import java.util.Optional;

public class ImplementsRelationCalculator {

    private TypeDao typeDao;

    private ImplementsDao implementsDao;

    public ImplementsRelationCalculator(TypeDao typeDao, ImplementsDao implementsDao) {
        this.typeDao = typeDao;
        this.implementsDao = implementsDao;
    }

    public void calculate(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, CompilationUnit compilationUnit) {
        List<ClassOrInterfaceType> implementedInterfaces = classOrInterfaceDeclaration.getImplementedTypes();
        for (ClassOrInterfaceType implementedInterface : implementedInterfaces) {
            String implementedInterfaceName = implementedInterface.getNameAsString();
            String implementedInterfacePackageName = implementedInterface
                    .findCompilationUnit()
                    .flatMap(CompilationUnit::getPackageDeclaration)
                    .flatMap(pkg -> Optional.of(pkg.getNameAsString())).orElse("???");
            if (typeDao.exist(implementedInterfaceName, implementedInterfacePackageName)) { // JDK interfaces are not indexed
                int interfaceId = typeDao.getId(implementedInterfaceName, implementedInterfacePackageName);
                String cuPackageName = compilationUnit.getPackageDeclaration().get().getNameAsString();
                int nbClasses = typeDao.count(classOrInterfaceDeclaration.getNameAsString(), cuPackageName);
                if (nbClasses > 1) {
                    System.err.println("More than one class having the same name '" + classOrInterfaceDeclaration.getName() + "' and package '" + cuPackageName + "'");
                } else {
                    int classId = typeDao.getId(classOrInterfaceDeclaration.getNameAsString(), cuPackageName);
                    implementsDao.save(new Implements(classId, interfaceId));
                }
            }
        }
    }
}
