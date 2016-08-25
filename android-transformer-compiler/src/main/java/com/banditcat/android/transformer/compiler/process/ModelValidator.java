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
package com.banditcat.android.transformer.compiler.process;


import com.banditcat.android.transformer.compiler.handler.AnnotationHandlers;
import com.banditcat.android.transformer.compiler.model.AnnotationElements;
import com.banditcat.android.transformer.compiler.model.AnnotationElementsHolder;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class ModelValidator {
    Messager mMessager;
    private AnnotationHandlers annotationHandlers;

    public ModelValidator(Messager messager, AnnotationHandlers annotationHandlers) {
        this.mMessager = messager;
        this.annotationHandlers = annotationHandlers;
    }



    public AnnotationElements validate(AnnotationElementsHolder extractedModel) throws Exception {


		/*
         * We currently do not validate the elements on the ancestors, assuming
		 * they've already been validated. This also means some checks such as
		 * unique ids might not be check all situations.
		 */
        AnnotationElementsHolder validatedElements = extractedModel.validatingHolder();

        for (String annotationName : annotationHandlers.getSupportedAnnotationTypes()) {
            //String validatorSimpleName = annotationHandler.getClass().getSimpleName();


            Set<? extends Element> annotatedElements = extractedModel.getRootAnnotatedElements(annotationName);

            Set<Element> validatedAnnotatedElements = new HashSet<Element>();

            validatedElements.putRootAnnotatedElements(annotationName, validatedAnnotatedElements);

            if (!annotatedElements.isEmpty()) {

            }

            for (Element annotatedElement : annotatedElements) {

                validatedAnnotatedElements.add(annotatedElement);
                mMessager.printMessage(Diagnostic.Kind.NOTE
                        , annotationName +" = " + annotatedElement.getSimpleName().toString());
            }
        }

        return validatedElements;
    }


}
