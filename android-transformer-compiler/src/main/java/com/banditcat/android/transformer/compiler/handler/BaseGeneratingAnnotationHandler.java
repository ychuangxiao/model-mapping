/**
 * Copyright (C) 2010-2014 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.banditcat.android.transformer.compiler.handler;



import com.banditcat.android.transformer.compiler.holder.GeneratedClassHolder;

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public abstract class BaseGeneratingAnnotationHandler<T extends GeneratedClassHolder> extends BaseAnnotationHandler<T> implements GeneratingAnnotationHandler<T> {

	public BaseGeneratingAnnotationHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
		super(targetClass, processingEnvironment);
	}

	public BaseGeneratingAnnotationHandler(String target, ProcessingEnvironment processingEnvironment) {
		super(target, processingEnvironment);
	}



	private boolean isInnerClass(Element element) {
		TypeElement typeElement = (TypeElement) element;
		return typeElement.getNestingKind().isNested();
	}


	public AnnotationMirror getAnnotationMirror(Element element, Class<?> annotationType) {
		AnnotationMirror result = null;

		String annotationClassName = annotationType.getName();
		for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
			if (mirror.getAnnotationType().toString().equals(annotationClassName)) {
				result = mirror;
				break;
			}
		}

		return result;
	}

	public AnnotationValue getAnnotationValue(AnnotationMirror annotation, String field) {
		if (annotation != null) {
			for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotation.getElementValues().entrySet()) {
				if (entry.getKey().getSimpleName().toString().equals(field)) {
					return entry.getValue();
				}
			}
		}

		return null;
	}
}
