
/**
 * Copyright (C) 2013-2016 banditcat
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
package com.banditcat.android.transformer.compiler.internal;


import com.banditcat.android.transformer.compiler.generation.CodeModelGenerator;
import com.banditcat.android.transformer.compiler.handler.AnnotationHandlers;
import com.banditcat.android.transformer.compiler.model.AnnotationElements;
import com.banditcat.android.transformer.compiler.model.AnnotationElementsHolder;
import com.banditcat.android.transformer.compiler.model.ModelExtractor;
import com.banditcat.android.transformer.compiler.model.OriginatingElements;
import com.banditcat.android.transformer.compiler.process.ModelProcessor;
import com.banditcat.android.transformer.compiler.process.ModelValidator;
import com.sun.codemodel.JCodeModel;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AndroidAnnotationsProcessor extends AbstractProcessor {


    RoundEnvironment roundEnvironment;

    Messager mMessager;

    Filer mFiler;

    AnnotationHandlers mAnnotationHandlers;

    OriginatingElements mOriginatingElements = new OriginatingElements();


    JCodeModel mCodeModel;

    public JCodeModel getCodeModel() {
        return mCodeModel;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mFiler = this.processingEnv.getFiler();
        mMessager = this.processingEnv.getMessager();

        mAnnotationHandlers = new AnnotationHandlers(processingEnv,mMessager);

        mCodeModel = new JCodeModel();
    }

    private boolean nothingToDo(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return roundEnv.processingOver() || annotations.size() == 0;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnvironment = roundEnv;


        mMessager.printMessage(Diagnostic.Kind.NOTE, "start process");

        if (nothingToDo(annotations, roundEnv)) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "no  process");

            return true;
        }

        try {


            AnnotationElementsHolder extractedModel = extractAnnotations(annotations, roundEnv);

            AnnotationElements validatedModel = validateAnnotations(extractedModel);

            ModelProcessor modelProcessor = new ModelProcessor(mMessager, processingEnv, mAnnotationHandlers);

            ModelProcessor.ProcessResult processResult = modelProcessor.process(validatedModel);


            generateSources(processResult);




        } catch (Exception e) {
            e.printStackTrace();
            mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }



        mMessager.printMessage(Diagnostic.Kind.NOTE, "end process");


        return true;
    }


    /**
     * 提取注解信息
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    private AnnotationElementsHolder extractAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        ModelExtractor modelExtractor = new ModelExtractor();


        AnnotationElementsHolder extractedModel = modelExtractor.extract(mMessager, annotations, getSupportedAnnotationTypes(), roundEnv);

        return extractedModel;


    }


    private AnnotationElements validateAnnotations(AnnotationElementsHolder extractedModel) throws Exception {

        mMessager.printMessage(Diagnostic.Kind.NOTE, "Validate Annotations Start");

        ModelValidator modelValidator = new ModelValidator(mMessager, mAnnotationHandlers);
        AnnotationElements validatedAnnotations = modelValidator.validate(extractedModel);

        mMessager.printMessage(Diagnostic.Kind.NOTE, "Validate Annotations End");
        return validatedAnnotations;
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return mAnnotationHandlers.getSupportedAnnotationTypes();

    }


    /**
     * 生成代码
     *
     * @param processResult
     * @throws IOException
     */
    private void generateSources(ModelProcessor.ProcessResult processResult) throws IOException {


        mMessager.printMessage(Diagnostic.Kind.NOTE, "Generate Sources Start");


        CodeModelGenerator modelGenerator = new CodeModelGenerator(processingEnv.getFiler(), "1.1");
        modelGenerator.generate(processResult);
        mMessager.printMessage(Diagnostic.Kind.NOTE, "Generate Sources End");
    }
}
