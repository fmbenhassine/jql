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
package io.github.benas.jcql.domain;

import io.github.benas.jcql.model.Method;
import org.springframework.jdbc.core.JdbcTemplate;

public class MethodDao extends BaseDao {

    public MethodDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public int save(Method method) {
        int id = getNextId("METHOD");
        jdbcTemplate.update("insert into METHOD values (?,?,?,?,?,?,?,?)", id, method.getName(),
                toSqliteBoolean(method.isPublic()),
                toSqliteBoolean(method.isStatic()),
                toSqliteBoolean(method.isFinal()),
                toSqliteBoolean(method.isAbstract()),
                toSqliteBoolean(method.isConstructor()),
                method.getTypeId());
        return id;
    }

}
