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
import io.github.benas.jcql.Database;
import io.github.benas.jcql.model.Field;
import io.github.benas.jcql.model.Interface;
import io.github.benas.jcql.model.Method;
import io.github.benas.jcql.model.Class;
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

    /*
     * TODO: the following is still an early unfinished POC to clean up
     */
    public void index(File sourceCodeDirectory, File databaseDirectory) throws IOException {
        System.out.println("Indexing source code in " + sourceCodeDirectory.getAbsolutePath() + " in database " + getDatabasePath(databaseDirectory));
        initDatabaseIn(databaseDirectory);
        Database database = new Database(databaseDirectory);

        Collection<File> files = listFiles(sourceCodeDirectory, new String[]{"java"}, true);
        int totalFiles = files.size();
        int fileIndex = 1;
        int cuId = 0;
        int classId = 0;
        int interfaceId = 0;
        int fieldId = 0;
        int methodId = 0;
        int parameterId = 0;
        for (File file : files) {
            try {
                CompilationUnit cu = parse(file);
                cuId++;
                io.github.benas.jcql.model.CompilationUnit compilationUnit = new io.github.benas.jcql.model.CompilationUnit(cuId, file.getName(), cu.getPackage().getPackageName());
                database.save(compilationUnit);
                List<TypeDeclaration> types = cu.getTypes();
                for (TypeDeclaration type : types) {
                    int modifiers = type.getModifiers();
                    boolean isInterface = isInterface(modifiers);
                    if (isInterface) {
                        interfaceId++;
                        Interface interfaze = new Interface(interfaceId, type.getName(), isFinal(modifiers), isPublic(modifiers), cuId);
                        database.save(interfaze);
                    } else {
                        classId++;
                        Class clazz = new Class(classId, type.getName(), isAbstract(modifiers), isFinal(modifiers), isPublic(modifiers), cuId);
                        database.save(clazz);
                    }

                    for (BodyDeclaration member : type.getMembers()) {
                        if (member instanceof FieldDeclaration) {
                            FieldDeclaration fieldDeclaration = (FieldDeclaration) member;
                            fieldId++;
                            int fieldModifiers = fieldDeclaration.getModifiers();
                            String name = fieldDeclaration.getVariables().get(0).getId().getName(); // TODO add support for multiple fields, ex: private String firstName, lastName;
                            database.save(new Field(fieldId, name, fieldDeclaration.getType().toString(),
                                    isPublic(fieldModifiers), isStatic(fieldModifiers), isFinal(fieldModifiers), isTransient(fieldModifiers), isInterface ? interfaceId : classId));
                        }
                        if (member instanceof MethodDeclaration) {
                            MethodDeclaration methodDeclaration = (MethodDeclaration) member;
                            methodId++;
                            int methodModifiers = methodDeclaration.getModifiers();
                            database.save(new Method(methodId, methodDeclaration.getName(), isAbstract(methodModifiers), isFinal(methodModifiers), isPublic(methodModifiers), isInterface ? interfaceId : classId));
                            List<Parameter> parameters = methodDeclaration.getParameters();
                            for (Parameter parameter : parameters) {
                                parameterId++;
                                io.github.benas.jcql.model.Parameter p = new io.github.benas.jcql.model.Parameter(parameterId, parameter.getId().getName(), parameter.getType().toString(), methodId);
                                database.save(p);
                            }
                        }
                    }
                }
            } catch (ParseException | IOException e) {
                System.err.println("Error while parsing " + file.getAbsolutePath());
            }
            System.out.print("\rIndexing files: " + getPercent(fileIndex, totalFiles) + "% " + ("(" + fileIndex + "/" + totalFiles + ")"));
            fileIndex++;
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

        Indexer indexer = new Indexer();
        indexer.index(sourceCodeDirectory, databaseDirectory);
    }


    private static void initDatabaseIn(File directory) throws IOException {
        File database = getFile(getDatabasePath(directory));
        deleteQuietly(database);
        touch(database);
        DataSource dataSource = getDataSourceFrom(directory);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        applyDDL(jdbcTemplate);
    }

    private static void applyDDL(JdbcTemplate jdbcTemplate) throws IOException {
        InputStream databaseSchema = Indexer.class.getClassLoader().getResourceAsStream("database.sql");
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(databaseSchema))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                jdbcTemplate.update(line);
            }
        }
    }

}