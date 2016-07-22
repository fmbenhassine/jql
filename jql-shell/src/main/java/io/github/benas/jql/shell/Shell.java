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
package io.github.benas.jql.shell;

import io.github.benas.jql.core.QueryExecutor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.io.Console;

import static io.github.benas.jql.Utils.getDataSourceFrom;

public class Shell {

    private static QueryExecutor queryExecutor;
    private static ResultSetExtractor<String> resultSetExtractor;

    public static void main(String[] args) {
        String databaseFile = "";
        if (args.length >= 1) {
            databaseFile = args[0];
        }

        DataSource dataSource = getDataSourceFrom(databaseFile);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        resultSetExtractor = new StringResultSetExtractor();
        queryExecutor = new QueryExecutor(jdbcTemplate);

        Console console = getConsole();

        String command;
        while (true) {
            System.out.print("$>");
            command = console.readLine();
            if (command.equalsIgnoreCase("quit")) {
                break;
            }
            process(command);
        }
        System.out.println("bye!");
    }

    private static void process(String command) {
        if (!command.trim().isEmpty()) {
            try {
                System.out.println(queryExecutor.execute(command, resultSetExtractor));
            } catch (DataAccessException e) {
                System.err.println("Unable to execute query: " + command);
                System.err.println(e.getRootCause().getMessage());
            }
        }
    }

    private static Console getConsole() {
        Console console = System.console();
        if (console == null) {
            System.err.print("No console available.");
            System.exit(1);
        }
        return console;
    }

}
