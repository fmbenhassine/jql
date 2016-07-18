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
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.github.benas.jcql.domain.*;
import io.github.benas.jcql.model.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.*;
import java.util.Collection;
import java.util.List;

import static com.github.javaparser.JavaParser.parse;
import static io.github.benas.jcql.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.apache.commons.io.FileUtils.*;

public class Indexer {

    private File databaseDirectory;
    private DatabaseInitializer databaseInitializer;

    private CompilationUnitDao compilationUnitDao;
    private TypeDao typeDao;
    private FieldDao fieldDao;
    private MethodDao methodDao;
    private ParameterDao parameterDao;
    private ExtendsDao extendsDao;
    private ImplementsDao implementsDao;

    public Indexer(File databaseDirectory) {
        DataSource dataSource = getDataSourceFrom(databaseDirectory);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        this.databaseDirectory = databaseDirectory;
        databaseInitializer = new DatabaseInitializer(databaseDirectory);
        compilationUnitDao = new CompilationUnitDao(jdbcTemplate);
        typeDao = new TypeDao(jdbcTemplate);
        fieldDao = new FieldDao(jdbcTemplate);
        methodDao = new MethodDao(jdbcTemplate);
        parameterDao = new ParameterDao(jdbcTemplate);
        extendsDao = new ExtendsDao(jdbcTemplate);
        implementsDao = new ImplementsDao(jdbcTemplate);
    }

    /*
     * TODO: the following is an early unfinished POC to clean up! A software craftsman never writes code like this ;-) :wink:
     */
    public void index(File sourceCodeDirectory) throws IOException {
        System.out.println("Indexing source code in " + sourceCodeDirectory.getAbsolutePath() + " in database " + getDatabasePath(databaseDirectory));
        databaseInitializer.initDatabase();

        Collection<File> files = listFiles(sourceCodeDirectory, new String[]{"java"}, true);
        int totalFiles = files.size();
        int fileIndex = 1;
        int cuId = 0;
        int typeId = 0;
        int fieldId = 0;
        int methodId = 0;
        int parameterId = 0;
        for (File file : files) {
            try {
                CompilationUnit cu = parse(file);
                cuId++;
                String packageName = cu.getPackage() != null ? cu.getPackage().getPackageName() : "";
                io.github.benas.jcql.model.CompilationUnit compilationUnit = new io.github.benas.jcql.model.CompilationUnit(cuId, file.getName(), packageName);
                compilationUnitDao.save(compilationUnit);
                List<TypeDeclaration> types = cu.getTypes();
                for (TypeDeclaration type : types) {
                    int modifiers = type.getModifiers();
                    boolean isInterface = type instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) type).isInterface();
                    boolean isAnnotation = type instanceof AnnotationDeclaration;
                    boolean isEnumeration = type instanceof EnumDeclaration;
                    boolean isClass = !isAnnotation && !isEnumeration && !isInterface;
                    typeId++;
                    Type t = new Type(typeId, type.getName(), isPublic(modifiers), isStatic(modifiers), isFinal(modifiers), isAbstract(modifiers), isClass, isInterface, isEnumeration, isAnnotation, cuId);
                    typeDao.save(t);

                    for (BodyDeclaration member : type.getMembers()) {
                        boolean isField = member instanceof FieldDeclaration;
                        boolean isMethod = member instanceof MethodDeclaration;
                        boolean isConstructor = member instanceof ConstructorDeclaration;
                        if (isField) {
                            FieldDeclaration fieldDeclaration = (FieldDeclaration) member;
                            int fieldModifiers = fieldDeclaration.getModifiers();
                            List<VariableDeclarator> variables = fieldDeclaration.getVariables();
                            for (VariableDeclarator variable : variables) {
                                String name = variable.getId().getName();
                                fieldId++;
                                fieldDao.save(new Field(fieldId, name, fieldDeclaration.getType().toString(),
                                        isPublic(fieldModifiers), isStatic(fieldModifiers), isFinal(fieldModifiers), isTransient(fieldModifiers), typeId));
                            }

                        }
                        if (isConstructor || isMethod) {
                            methodId++;
                            int methodModifiers = isMethod ? ((MethodDeclaration) member).getModifiers() : ((ConstructorDeclaration) member).getModifiers();
                            List<Parameter> parameters = isMethod ? ((MethodDeclaration) member).getParameters() : ((ConstructorDeclaration) member).getParameters();
                            String name = isMethod ? ((MethodDeclaration) member).getName() : ((ConstructorDeclaration) member).getName();
                            methodDao.save(new Method(methodId, name,
                                    isPublic(methodModifiers), isStatic(methodModifiers), isFinal(methodModifiers), isAbstract(methodModifiers), isConstructor, typeId));
                            for (Parameter parameter : parameters) {
                                parameterId++;
                                io.github.benas.jcql.model.Parameter p = new io.github.benas.jcql.model.Parameter(parameterId, parameter.getId().getName(), parameter.getType().toString(), methodId);
                                parameterDao.save(p);
                            }
                        }
                        if (member instanceof AnnotationMemberDeclaration) {
                            AnnotationMemberDeclaration annotationMemberDeclaration = (AnnotationMemberDeclaration) member;
                            methodId++;
                            int annotationMemberModifiers = annotationMemberDeclaration.getModifiers();
                            methodDao.save(new Method(methodId, annotationMemberDeclaration.getName(),
                                    isPublic(annotationMemberModifiers), isStatic(annotationMemberModifiers), isFinal(annotationMemberModifiers), isAbstract(annotationMemberModifiers), false, typeId));
                        }
                    }
                }
            } catch (ParseException | IOException e) {
                System.err.println("Error while parsing " + file.getAbsolutePath());
            }
            System.out.print("\rIndexing files: " + getPercent(fileIndex, totalFiles) + "% " + ("(" + fileIndex + "/" + totalFiles + ")"));
            fileIndex++;
        }

        // FIXME Inefficient 2 phases algorithm:
        // phase 1: index all types (classes, interfaces, fields, methods, etc)
        // phase 2: calculate relations (implements, extends) once all IDs have been assigned in phase 1
        // TODO to optimize in one phase if possible
        // TODO Looks like Sqlite3 is thread safe (Q6 in https://www.sqlite.org/faq.html). The 2nd step (calculating relations) can be done in parallel
        System.out.println();
        System.out.println("Indexing relations..");
        for (File file : files) {
            try {
                CompilationUnit cu = parse(file);
                List<TypeDeclaration> types = cu.getTypes();
                for (TypeDeclaration type : types) {
                    boolean isInterface = type instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) type).isInterface();
                    boolean isAnnotation = type instanceof AnnotationDeclaration;
                    boolean isEnumeration = type instanceof EnumDeclaration;
                    if (isInterface) {
                        // check if this interface extends another interface and persist relation in EXTENDS table
                        ClassOrInterfaceDeclaration interfaceDeclaration = (ClassOrInterfaceDeclaration) type;
                        List<ClassOrInterfaceType> extendedInterfaces = interfaceDeclaration.getExtends();
                        for (ClassOrInterfaceType extendedInterface : extendedInterfaces) {
                            String extendedInterfaceName = extendedInterface.getName();
                            String extendedInterfacePackageName = ((CompilationUnit) extendedInterface.getParentNode().getParentNode()).getPackage().getPackageName();
                            if (typeDao.existInterface(extendedInterfaceName, extendedInterfacePackageName)) { // JDK interfaces are not indexed
                                int extendedInterfaceId = typeDao.getInterfaceId(extendedInterfaceName, extendedInterfacePackageName);
                                int interfaceId = typeDao.getInterfaceId(interfaceDeclaration.getName(), cu.getPackage().getPackageName());
                                extendsDao.save(new Extends(interfaceId, extendedInterfaceId));
                            }
                        }
                    } else {
                        if (!isAnnotation && !isEnumeration) {
                            ClassOrInterfaceDeclaration classDeclaration = (ClassOrInterfaceDeclaration) type;

                            // check if this class implements an interface and persist relation in IMPLEMENTS table
                            List<ClassOrInterfaceType> implementedInterfaces = classDeclaration.getImplements();
                            for (ClassOrInterfaceType implementedInterface : implementedInterfaces) {
                                String implementedInterfaceName = implementedInterface.getName();
                                String implementedInterfacePackageName = ((CompilationUnit) implementedInterface.getParentNode().getParentNode()).getPackage().getPackageName();
                                if (typeDao.existInterface(implementedInterfaceName, implementedInterfacePackageName)) { // JDK interfaces are not indexed
                                    int interfaceId = typeDao.getInterfaceId(implementedInterfaceName, implementedInterfacePackageName);
                                    int nbClasses = typeDao.countClass(type.getName(), cu.getPackage().getPackageName());
                                    if (nbClasses > 1) {
                                        System.err.println("More than one class having the same name '" + type.getName() + "' and package '" + cu.getPackage().getPackageName() + "'");
                                    } else {
                                        int classId = typeDao.getClassId(type.getName(), cu.getPackage().getPackageName());
                                        implementsDao.save(new Implements(classId, interfaceId));
                                    }
                                }
                            }
                            // check if this class extends another class and persist relation in EXTENDS table
                            List<ClassOrInterfaceType> extendedClasses = classDeclaration.getExtends(); // should contain at most one extended class, no multiple inheritance in java
                            for (ClassOrInterfaceType extendedClass : extendedClasses) {
                                String extendedClassName = extendedClass.getName();
                                String extendedClassPackageName = ((CompilationUnit) extendedClass.getParentNode().getParentNode()).getPackage().getPackageName();
                                if (typeDao.existClass(extendedClassName, extendedClassPackageName)) { // JDK classes are not indexed
                                    int extendedClassId = typeDao.getClassId(extendedClassName, extendedClassPackageName);
                                    int nbClasses = typeDao.countClass(type.getName(), cu.getPackage().getPackageName());
                                    if (nbClasses > 1) {
                                        System.err.println("More than one class having the same name '" + type.getName() + "' and package '" + cu.getPackage().getPackageName() + "'");
                                    } else {
                                        int classId = typeDao.getClassId(classDeclaration.getName(), cu.getPackage().getPackageName());
                                        extendsDao.save(new Extends(classId, extendedClassId));
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (ParseException | IOException e) {
                System.err.println("Error while parsing " + file.getAbsolutePath());
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        String sourceCodeDir = "";
        String databaseDir = "";
        if (args.length >= 1) {
            sourceCodeDir = args[0];
        }
        if (args.length >= 2) {
            databaseDir = args[1];
        }

        File sourceCodeDirectory = new File(sourceCodeDir);
        File databaseDirectory = new File(databaseDir);

        Indexer indexer = new Indexer(databaseDirectory);
        indexer.index(sourceCodeDirectory);
    }




}