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
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import static com.github.javaparser.JavaParser.parse;
import static io.github.benas.jcql.Utils.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.FileUtils.*;

public class Indexer {

    public void index(File sourceCodeDirectory, File databaseDirectory) throws IOException, URISyntaxException {
        System.out.println("Indexing source code in " + sourceCodeDirectory.getAbsolutePath() + " in database " + getDatabasePath(databaseDirectory));
        initDatabaseIn(databaseDirectory);

        DataSource dataSource = getDataSourceFrom(databaseDirectory);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Collection<File> files = listFiles(sourceCodeDirectory, new String[]{"java"}, true);
        int totalFiles = files.size();
        int fileIndex = 1;
        int typeId = 0;
        for (File file : files) {
            try {
                CompilationUnit cu = parse(file);
                List<TypeDeclaration> types = cu.getTypes();
                for (TypeDeclaration type : types) {
                    jdbcTemplate.update("insert into CLASS values (?,?)", typeId++, type.getName());
                    int methodId = 0;
                    for (BodyDeclaration member : type.getMembers()) {
                        if (member instanceof MethodDeclaration) {
                            MethodDeclaration method = (MethodDeclaration) member;
                            jdbcTemplate.update("insert into METHOD values (?,?)", methodId++, method.getName());
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

    public static void main(String[] args) throws IOException, URISyntaxException {
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


    private static void initDatabaseIn(File directory) throws IOException, URISyntaxException {
        File database = getFile(getDatabasePath(directory));
        deleteQuietly(database);
        touch(database);
        DataSource dataSource = getDataSourceFrom(directory);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        File schema = new File(Indexer.class.getResource("/database.sql").toURI());
        List<String> ddl = readLines(schema, defaultCharset());
        ddl.forEach(jdbcTemplate::update);
    }

}