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

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.github.javaparser.JavaParser.parse;
import static io.github.benas.jcql.Utils.getPercent;

public class RelationCalculator {

    private ExtendsRelationCalculator extendsRelationCalculator;
    private ImplementsRelationCalculator implementsRelationCalculator;
    private AnnotatedWithCalculator annotatedWithCalculator;

    public RelationCalculator(ExtendsRelationCalculator extendsRelationCalculator, ImplementsRelationCalculator implementsRelationCalculator, AnnotatedWithCalculator annotatedWithCalculator) {
        this.extendsRelationCalculator = extendsRelationCalculator;
        this.implementsRelationCalculator = implementsRelationCalculator;
        this.annotatedWithCalculator = annotatedWithCalculator;
    }

    public void calculateRelations(Collection<File> files) {
        System.out.println();
        int totalFiles = files.size();
        int fileIndex = 1;
        for (File file : files) {
            try {
                CompilationUnit cu = parse(file);
                List<TypeDeclaration> types = cu.getTypes();
                for (TypeDeclaration type : types) {
                    boolean isInterface = type instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) type).isInterface();
                    boolean isAnnotation = type instanceof AnnotationDeclaration;
                    boolean isEnumeration = type instanceof EnumDeclaration;
                    boolean isClass = !isAnnotation && !isEnumeration && !isInterface;
                    if (isInterface) {
                        // check if this interface extends another interface and persist relation in EXTENDS table
                        ClassOrInterfaceDeclaration interfaceDeclaration = (ClassOrInterfaceDeclaration) type;
                        extendsRelationCalculator.calculate(interfaceDeclaration, cu);
                    }
                    if (isClass) {
                        ClassOrInterfaceDeclaration classDeclaration = (ClassOrInterfaceDeclaration) type;
                        // check if this class implements an interface and persist relation in IMPLEMENTS table
                        implementsRelationCalculator.calculate(classDeclaration, cu);
                        // check if this class extends another class and persist relation in EXTENDS table
                        extendsRelationCalculator.calculate(classDeclaration, cu);
                    }
                    if (isClass || isInterface) {
                        annotatedWithCalculator.calculate((ClassOrInterfaceDeclaration) type, cu);
                    }
                }
            } catch (ParseException | IOException e) {
                System.err.println("Error while parsing " + file.getAbsolutePath());
            }
            System.out.print("\rCalculating relations: " + getPercent(fileIndex, totalFiles) + "% " + ("(" + fileIndex + "/" + totalFiles + ")"));
            fileIndex++;
        }
        System.out.println();
    }
}
