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
package io.github.benas.jcql.core;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import io.github.benas.jcql.domain.MethodDao;
import io.github.benas.jcql.model.Method;

import java.util.List;

import static java.lang.reflect.Modifier.*;

public class ConstructorIndexer {

    private MethodDao methodDao;
    private ParameterIndexer parameterIndexer;

    public ConstructorIndexer(MethodDao methodDao, ParameterIndexer parameterIndexer) {
        this.methodDao = methodDao;
        this.parameterIndexer = parameterIndexer;
    }

    public void index(ConstructorDeclaration constructorDeclaration, int typeId) {
        int modifiers = constructorDeclaration.getModifiers();
        List<Parameter> parameters = constructorDeclaration.getParameters();
        String name = constructorDeclaration.getName();
        int methodId = methodDao.save(new Method(name,
                isPublic(modifiers), isStatic(modifiers), isFinal(modifiers), isAbstract(modifiers), true, typeId));
        for (Parameter parameter : parameters) {
            parameterIndexer.index(parameter, methodId);
        }
    }

}
