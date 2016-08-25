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

import com.banditcat.android.transformer.compiler.helper.APTCodeModelHelper;
import com.banditcat.android.transformer.compiler.holder.GeneratedClassHolder;
import com.banditcat.android.transformer.compiler.model.AnnotationElements;
import com.banditcat.android.transformer.compiler.process.ProcessHolder;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JType;


import java.util.Collection;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

public abstract class BaseAnnotationHandler<T extends GeneratedClassHolder> implements AnnotationHandler<T> {

	private final String target;
	protected ProcessingEnvironment processingEnv;

	protected AnnotationElements validatedModel;
	protected ProcessHolder processHolder;

	protected APTCodeModelHelper codeModelHelper ;

	public BaseAnnotationHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
		this(targetClass.getCanonicalName(), processingEnvironment);
		this.codeModelHelper = new APTCodeModelHelper();
	}

	public BaseAnnotationHandler(String target, ProcessingEnvironment processingEnvironment) {
		this.target = target;
		this.processingEnv = processingEnvironment;
		this.codeModelHelper = new APTCodeModelHelper();
	}



	@Override
	public void setValidatedModel(AnnotationElements validatedModel) {
		this.validatedModel = validatedModel;
	}

	@Override
	public void setProcessHolder(ProcessHolder processHolder) {
		this.processHolder = processHolder;
	}

	public ProcessingEnvironment processingEnvironment() {
		return processHolder.processingEnvironment();
	}

	public ProcessHolder.Classes classes() {
		return processHolder.classes();
	}

	public JCodeModel codeModel() {
		return processHolder.codeModel();
	}

	public JClass refClass(String fullyQualifiedClassName) {
		return processHolder.refClass(fullyQualifiedClassName);
	}

	public JClass refClass(Class<?> clazz) {
		return processHolder.refClass(clazz);
	}

	public void generateApiClass(Element originatingElement, Class<?> apiClass) {
		processHolder.generateApiClass(originatingElement, apiClass);
	}

	@Override
	public String getTarget() {
		return target;
	}


	public JMethod getMethod(Collection<JMethod> methods, String name, JType[] argTypes) {
		for (JMethod m : methods) {
			if (!m.name().equals(name))
				continue;

			if (hasSignature(m, argTypes))
				return m;
		}
		return null;
	}

	public boolean hasSignature(JMethod jMethod, JType[] argTypes) {
		JType[] p = jMethod.listParamTypes();
		if (p.length != argTypes.length)
			return false;

		for (int i = 0; i < p.length; i++) {

			if (!p[i].name().equals(argTypes[i].name())) {
				return false;
			}
		}


		return true;
	}

	public TypeElement getTypeElement(AnnotationValue value) {
		TypeElement result = null;

		if (value != null) {
			TypeMirror typeMirror = (TypeMirror) value.getValue();
			Types TypeUtils = processingEnv.getTypeUtils();
			result = (TypeElement) TypeUtils.asElement(typeMirror);
		}

		return result;
	}

}
