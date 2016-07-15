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
package io.github.benas.jcql;

import io.github.benas.jcql.model.*;
import io.github.benas.jcql.model.Class;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.io.File;

import static io.github.benas.jcql.Utils.getDataSourceFrom;

public class Database {

    private JdbcTemplate jdbcTemplate;

    public Database(File databaseDirectory) {
        DataSource dataSource = getDataSourceFrom(databaseDirectory);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void save(CompilationUnit compilationUnit) {
        jdbcTemplate.update("insert into COMPILATION_UNIT values (?,?,?)", compilationUnit.getId(), compilationUnit.getFileName(), compilationUnit.getPackageDeclaration());
    }

    public void save(Class type) {
        jdbcTemplate.update("insert into CLASS values (?,?,?,?,?,?)", type.getId(), type.getName(), toSqliteBoolean(type.isAbstract()), toSqliteBoolean(type.isFinal()), toSqliteBoolean(type.isPublic()), type.getCompilationUnitId());
    }

    public void save(Interface type) {
        jdbcTemplate.update("insert into INTERFACE values (?,?,?,?,?)", type.getId(), type.getName(), toSqliteBoolean(type.isFinal()), toSqliteBoolean(type.isPublic()), type.getCompilationUnitId());
    }

    public void save(Field field) {
        jdbcTemplate.update("insert into FIELD values (?,?,?,?,?,?,?,?)",
                field.getId(), field.getName(), field.getType(),
                toSqliteBoolean(field.isPublic()), toSqliteBoolean(field.isStatic()), toSqliteBoolean(field.isFinal()), toSqliteBoolean(field.isTransient()), field.getTypeId());
    }

    public void save(Method method) {
        jdbcTemplate.update("insert into METHOD values (?,?,?,?,?,?)", method.getId(), method.getName(), toSqliteBoolean(method.isAbstract()), toSqliteBoolean(method.isFinal()), toSqliteBoolean(method.isPublic()), method.getTypeId());
    }

    public void save(Parameter parameter) {
        jdbcTemplate.update("insert into PARAMETER values (?,?,?,?)", parameter.getId(), parameter.getName(), parameter.getType(), parameter.getMethodId());
    }

    // SQLITE 3 does not support boolean ..
    private int toSqliteBoolean(boolean bool) {
        return bool ? 1 : 0;
    }

}
