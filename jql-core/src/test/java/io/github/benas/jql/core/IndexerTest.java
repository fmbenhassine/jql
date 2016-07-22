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

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;

import static io.github.benas.jql.Utils.getDataSourceFrom;
import static org.assertj.core.api.Assertions.assertThat;

public class IndexerTest {

    private File sourceCodeDirectory;

    private JdbcTemplate jdbcTemplate;

    private Indexer indexer;

    @Before
    public void setUp() throws Exception {
        File databaseDirectory = new File("target").getAbsoluteFile();
        sourceCodeDirectory = new File("src/test/java/io/github/benas/jql/code").getAbsoluteFile();
        indexer = new Indexer(databaseDirectory);
        jdbcTemplate = new JdbcTemplate(getDataSourceFrom(databaseDirectory));
    }

    @Test
    public void testCodeIndexing() throws Exception {
        indexer.index(sourceCodeDirectory);

        assertThat(count("class")).isEqualTo(2);
        assertThat(count("interface")).isEqualTo(2);
        assertThat(count("annotation")).isEqualTo(1);
        assertThat(count("enumeration")).isEqualTo(1);
        assertThat(count("field")).isEqualTo(3);
        assertThat(count("constructor")).isEqualTo(1);
        assertThat(count("method")).isEqualTo(7);
        assertThat(count("parameter")).isEqualTo(2);
        assertThat(count("implements")).isEqualTo(2);
        assertThat(count("extends")).isEqualTo(1);
        assertThat(count("annotated_with")).isEqualTo(1);
        assertThat(count("compilation_unit")).isEqualTo(7);
    }

    private int count(String table) {
        return jdbcTemplate.queryForObject("select count(*) from " + table, Integer.class);
    }
}
