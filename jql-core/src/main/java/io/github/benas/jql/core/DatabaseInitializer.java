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

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.*;

import static io.github.benas.jql.Utils.getDataSourceFrom;
import static io.github.benas.jql.Utils.getDatabasePath;
import static org.apache.commons.io.FileUtils.*;

public class DatabaseInitializer {

    private File databaseDirectory;

    private JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(File databaseDirectory) {
        this.databaseDirectory = databaseDirectory;
        DataSource dataSource = getDataSourceFrom(databaseDirectory);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void initDatabase() throws IOException {
        File database = getFile(getDatabasePath(databaseDirectory));
        deleteQuietly(database);
        touch(database);
        applyDDL("database.sql");
    }

    private void applyDDL(String schema) throws IOException {
        InputStream databaseSchema = Indexer.class.getClassLoader().getResourceAsStream(schema);
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(databaseSchema))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                jdbcTemplate.update(line);
            }
        }
    }
}
