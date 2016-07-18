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

    public void save(Type type) {
        jdbcTemplate.update("insert into TYPE values (?,?,?,?,?,?,?,?,?,?,?)", type.getId(), type.getName(),
                toSqliteBoolean(type.isPublic()),
                toSqliteBoolean(type.isStatic()),
                toSqliteBoolean(type.isFinal()),
                toSqliteBoolean(type.isAbstract()),
                toSqliteBoolean(type.isClass()),
                toSqliteBoolean(type.isInterface()),
                toSqliteBoolean(type.isEnumeration()),
                toSqliteBoolean(type.isAnnotation()),
                type.getCompilationUnitId());
    }

    public void save(Field field) {
        jdbcTemplate.update("insert into FIELD values (?,?,?,?,?,?,?,?)",
                field.getId(), field.getName(), field.getType(),
                toSqliteBoolean(field.isPublic()), toSqliteBoolean(field.isStatic()), toSqliteBoolean(field.isFinal()), toSqliteBoolean(field.isTransient()), field.getTypeId());
    }

    public void save(Method method) {
        jdbcTemplate.update("insert into METHOD values (?,?,?,?,?,?,?,?)", method.getId(), method.getName(),
                toSqliteBoolean(method.isPublic()),
                toSqliteBoolean(method.isStatic()),
                toSqliteBoolean(method.isFinal()),
                toSqliteBoolean(method.isAbstract()),
                toSqliteBoolean(method.isConstructor()),
                method.getTypeId());
    }

    public void save(Parameter parameter) {
        jdbcTemplate.update("insert into PARAMETER values (?,?,?,?)", parameter.getId(), parameter.getName(), parameter.getType(), parameter.getMethodId());
    }

    public void save(Implements anImplements) {
        jdbcTemplate.update("insert into IMPLEMENTS values (?,?)", anImplements.getClassId(), anImplements.getInterfaceId());
    }

    public void save(Extends anExtends) {
        jdbcTemplate.update("insert into EXTENDS values (?,?)", anExtends.getTypeId(), anExtends.getExtendedTypeId());
    }

    public int count(String table) {
        return jdbcTemplate.queryForObject("select count(*) from " + table, Integer.class);
    }

    public int countClass(String name, String packageName) {
        return jdbcTemplate.queryForObject("select count(c.id) from class c, compilation_unit cu where c.cu_id = cu.id and c.name = ? and cu.package = ?", new Object[]{name, packageName}, Integer.class);
    }

    public int getInterfaceId(String name, String packageName) {
        return jdbcTemplate.queryForObject("select i.id from interface i, compilation_unit cu where i.cu_id = cu.id and i.name = ? and cu.package = ?", new Object[]{name, packageName}, Integer.class);
    }

    public int getClassId(String name, String packageName) {
        return jdbcTemplate.queryForObject("select c.id from class c, compilation_unit cu where c.cu_id = cu.id and c.name = ? and cu.package = ?", new Object[]{name, packageName}, Integer.class);
    }

    public boolean existInterface(String name, String packageName) {
        return jdbcTemplate.queryForObject("select count(i.id) from interface i, compilation_unit cu where i.cu_id = cu.id and i.name = ? and cu.package = ?", new Object[]{name, packageName}, Integer.class) == 1;
    }

    public boolean existClass(String name, String packageName) {
        return jdbcTemplate.queryForObject("select count(c.id) from class c, compilation_unit cu where c.cu_id = cu.id and c.name = ? and cu.package = ?", new Object[]{name, packageName}, Integer.class) == 1;
    }

    // SQLITE 3 does not support boolean ..
    private int toSqliteBoolean(boolean bool) {
        return bool ? 1 : 0;
    }

    private boolean fromSqliteBoolean(int bool) {
        return bool == 1;
    }

}
