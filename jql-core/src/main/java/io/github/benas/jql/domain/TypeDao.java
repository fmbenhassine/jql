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
package io.github.benas.jql.domain;

import io.github.benas.jql.model.Type;
import org.springframework.jdbc.core.JdbcTemplate;

public class TypeDao extends BaseDao {

    public TypeDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public int save(Type type) {
        int typeId = getNextId("TYPE");
        jdbcTemplate.update("insert into TYPE values (?,?,?,?,?,?,?,?,?,?,?)", typeId, type.getName(),
                toSqliteBoolean(type.isPublic()),
                toSqliteBoolean(type.isStatic()),
                toSqliteBoolean(type.isFinal()),
                toSqliteBoolean(type.isAbstract()),
                toSqliteBoolean(type.isClass()),
                toSqliteBoolean(type.isInterface()),
                toSqliteBoolean(type.isEnumeration()),
                toSqliteBoolean(type.isAnnotation()),
                type.getCompilationUnitId());
        return typeId;
    }

    public int count(String name, String packageName) {
        return jdbcTemplate.queryForObject("select count(t.id) from type t, compilation_unit cu where t.cu_id = cu.id and t.name = ? and cu.package = ?", new Object[]{name, packageName}, Integer.class);
    }

    public int getId(String name, String packageName) {
        return jdbcTemplate.queryForObject("select t.id from type t, compilation_unit cu where t.cu_id = cu.id and t.name = ? and cu.package = ?", new Object[]{name, packageName}, Integer.class);
    }

    public boolean exist(String name, String packageName) {
        return jdbcTemplate.queryForObject("select count(t.id) from type t, compilation_unit cu where t.cu_id = cu.id and t.name = ? and cu.package = ?", new Object[]{name, packageName}, Integer.class) == 1;
    }

}
