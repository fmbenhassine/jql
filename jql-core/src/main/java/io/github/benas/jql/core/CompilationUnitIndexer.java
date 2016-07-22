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

import com.github.javaparser.ast.body.TypeDeclaration;
import io.github.benas.jql.domain.CompilationUnitDao;

import java.util.List;

public class CompilationUnitIndexer {

    private TypeIndexer typeIndexer;

    private CompilationUnitDao compilationUnitDao;

    public CompilationUnitIndexer(CompilationUnitDao compilationUnitDao, TypeIndexer typeIndexer) {
        this.compilationUnitDao = compilationUnitDao;
        this.typeIndexer = typeIndexer;
    }

    public void index(com.github.javaparser.ast.CompilationUnit compilationUnit, String fileName) {
        String packageName = compilationUnit.getPackage() != null ? compilationUnit.getPackage().getPackageName() : "";
        io.github.benas.jql.model.CompilationUnit cu = new io.github.benas.jql.model.CompilationUnit(fileName, packageName);
        int cuId =  compilationUnitDao.save(cu);
        List<TypeDeclaration> types = compilationUnit.getTypes();
        for (TypeDeclaration type : types) {
            typeIndexer.index(type, cuId);
        }
    }

}
