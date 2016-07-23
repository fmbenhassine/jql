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

import io.github.benas.jql.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static io.github.benas.jql.Utils.getDataSourceFrom;
import static io.github.benas.jql.Utils.getDatabasePath;
import static org.apache.commons.io.FileUtils.listFiles;

public class Indexer {

    private File databaseDirectory;
    private DatabaseInitializer databaseInitializer;
    private EntityIndexer entityIndexer;
    private RelationCalculator relationCalculator;

    public Indexer(File databaseDirectory) {
        DataSource dataSource = getDataSourceFrom(databaseDirectory);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        this.databaseDirectory = databaseDirectory;
        databaseInitializer = new DatabaseInitializer(databaseDirectory);
        CompilationUnitIndexer compilationUnitIndexer = getCompilationUnitIndexer(jdbcTemplate);
        entityIndexer = new EntityIndexer(compilationUnitIndexer);
        TypeDao typeDao = new TypeDao(jdbcTemplate);
        relationCalculator = new RelationCalculator(
                new ExtendsRelationCalculator(typeDao, new ExtendsDao(jdbcTemplate)),
                new ImplementsRelationCalculator(typeDao, new ImplementsDao(jdbcTemplate)),
                new AnnotatedWithCalculator(typeDao, new AnnotatedWithDao(jdbcTemplate))
                );
    }

    public void index(File sourceCodeDirectory) throws IOException {
        System.out.println("Indexing source code in " + sourceCodeDirectory.getAbsolutePath() + " into " + getDatabasePath(databaseDirectory));
        databaseInitializer.initDatabase();

        Collection<File> files = listFiles(sourceCodeDirectory, new String[]{"java"}, true);
        entityIndexer.indexFiles(files);
        relationCalculator.calculateRelations(files);
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

    private CompilationUnitIndexer getCompilationUnitIndexer(JdbcTemplate jdbcTemplate) {
        // TODO Hmmm, looks like I need a DI container
        TypeDao typeDao = new TypeDao(jdbcTemplate);
        FieldDao fieldDao = new FieldDao(jdbcTemplate);
        MethodDao methodDao = new MethodDao(jdbcTemplate);
        ParameterDao parameterDao = new ParameterDao(jdbcTemplate);
        ParameterIndexer parameterIndexer = new ParameterIndexer(parameterDao);
        return new CompilationUnitIndexer(new CompilationUnitDao(jdbcTemplate),
                new TypeIndexer(typeDao,
                        new BodyDeclarationIndexer(
                                new FieldIndexer(fieldDao),
                                new ConstructorIndexer(methodDao, parameterIndexer),
                                new MethodIndexer(methodDao, parameterIndexer),
                                new AnnotationMemberIndexer(methodDao))));
    }

}