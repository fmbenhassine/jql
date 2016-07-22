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

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCountCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

class StringResultSetExtractor implements ResultSetExtractor<String> {

    @Override
    public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        RowCountCallbackHandler rowCountCallbackHandler = new RowCountCallbackHandler();
        rowCountCallbackHandler.processRow(resultSet);
        int columnCount = resultSet.getMetaData().getColumnCount();
        List<String> columnNames = asList(rowCountCallbackHandler.getColumnNames());
        String header = getHeader(columnNames);

        StringBuilder result = new StringBuilder(header);
        result.append("\n");

        while (resultSet.next()) {
            StringBuilder stringBuilder = new StringBuilder();
            int i = 1;
            while (i <= columnCount) {
                stringBuilder.append(resultSet.getObject(i));
                if (i < columnCount) {
                    stringBuilder.append(" | ");
                }
                i++;
            }
            result.append(stringBuilder.toString()).append("\n");
        }

        return result.toString();
    }

    private String getHeader(List<String> columnNames) {
        Iterator<String> iterator = columnNames.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            String columnName = iterator.next();
            stringBuilder.append(columnName);
            if (iterator.hasNext()) {
                stringBuilder.append(" | ");
            }
        }
        return stringBuilder.toString();
    }
}
