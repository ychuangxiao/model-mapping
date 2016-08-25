/**
 * Copyright (C) 2010-2014 eBusiness Information, Excilys Group
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.banditcat.android.transformer.compiler.generation;

import com.banditcat.android.transformer.compiler.model.OriginatingElements;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

public class SourceCodewriter extends CodeWriter {

    private static final VoidOutputStream VOID_OUTPUT_STREAM = new VoidOutputStream();

    private final Filer filer;
    private OriginatingElements originatingElements;

    private static class VoidOutputStream extends OutputStream {
        @Override
        public void write(int arg0) throws IOException {
            // Do nothing
        }
    }

    public SourceCodewriter(Filer filer, OriginatingElements originatingElements) {
        this.filer = filer;
        this.originatingElements = originatingElements;
    }

    @Override
    public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
        String qualifiedClassName = toQualifiedClassName(pkg, fileName);

        Element[] classOriginatingElements = originatingElements.getClassOriginatingElements(qualifiedClassName);

        try {
            JavaFileObject sourceFile;

            if (classOriginatingElements.length == 0) {
            }

            sourceFile = filer.createSourceFile(qualifiedClassName, classOriginatingElements);

            return sourceFile.openOutputStream();
        } catch (FilerException e) {

            return VOID_OUTPUT_STREAM;
        }
    }

    private String toQualifiedClassName(JPackage pkg, String fileName) {
        int suffixPosition = fileName.lastIndexOf('.');
        String className = fileName.substring(0, suffixPosition);

        String qualifiedClassName = pkg.name() + "." + className;
        return qualifiedClassName;
    }

    @Override
    public void close() throws IOException {
    }
}
