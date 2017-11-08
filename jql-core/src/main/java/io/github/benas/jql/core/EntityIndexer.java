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

import com.github.javaparser.ParseException;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static com.github.javaparser.JavaParser.parse;
import static io.github.benas.jql.Utils.getPercent;

public class EntityIndexer {

    private CompilationUnitIndexer compilationUnitIndexer;

    public EntityIndexer(CompilationUnitIndexer compilationUnitIndexer) {
        this.compilationUnitIndexer = compilationUnitIndexer;
    }

    public void indexFiles(Collection<File> files) {
        int totalFiles = files.size();
        int fileIndex = 1;
        for (File file : files) {
            try {
                CompilationUnit cu = parse(file);
                compilationUnitIndexer.index(cu, file.getName());
            } catch (ParseProblemException | IOException e) {
                System.err.println("Error while parsing " + file.getAbsolutePath());
            }
            System.out.print("\rIndexing entities: " + getPercent(fileIndex, totalFiles) + "% " + ("(" + fileIndex + "/" + totalFiles + ")"));
            fileIndex++;
        }
    }
}
