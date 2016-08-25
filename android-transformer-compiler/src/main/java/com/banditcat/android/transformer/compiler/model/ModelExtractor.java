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
package com.banditcat.android.transformer.compiler.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

public class ModelExtractor {
    Messager mMessager;

    /**
     * Extracts annotated elements on elements given to the annotation processor
     * as well as annotations in their superclasses
     */
    public AnnotationElementsHolder extract(
            Messager messager,
            Set<? extends TypeElement> annotations
            , Set<String> annotationTypesToCheck
            , RoundEnvironment roundEnv) {

        mMessager = messager;


        AnnotationElementsHolder extractedModel = new AnnotationElementsHolder();

        Set<? extends Element> rootElements = roundEnv.getRootElements();

        Set<TypeElement> rootTypeElements = findRootTypeElements(rootElements);

        extractAncestorsAnnotations(extractedModel, annotationTypesToCheck, rootTypeElements);

        extractRootElementsAnnotations(annotations, roundEnv, extractedModel);

        return extractedModel;
    }

    /**
     * Adds all root elements that are type element, and adds their enclosing
     * type if they are not type elements (for annotated elements such as fields
     * and methods).
     */
    private Set<TypeElement> findRootTypeElements(Set<? extends Element> rootElements) {
        Set<TypeElement> rootTypeElements = new HashSet<TypeElement>();
        for (Element element : rootElements) {
            if (element instanceof TypeElement) {
                rootTypeElements.add((TypeElement) element);
            } else {
                Element enclosingElement = element.getEnclosingElement();
                if (enclosingElement instanceof TypeElement) {
                    rootTypeElements.add((TypeElement) enclosingElement);
                }
            }

            mMessager.printMessage(Diagnostic.Kind.NOTE, "findRootTypeElements:" + element.getSimpleName());
        }
        return rootTypeElements;
    }

    /**
     * 提取基类注解信息
     *
     * @param extractedModel 注解元素拥有者
     * @param annotationTypesToCheck 注解类型集合信息
     * @param rootTypeElements
     */
    private void extractAncestorsAnnotations(AnnotationElementsHolder extractedModel, Set<String> annotationTypesToCheck, Set<TypeElement> rootTypeElements) {
        for (TypeElement rootTypeElement : rootTypeElements) {
            Set<TypeElement> ancestors = new HashSet<TypeElement>();
            addAncestorsElements(ancestors, rootTypeElement);
            if (!ancestors.isEmpty()) {

                for (TypeElement ancestor : ancestors) {
                    extractAnnotations(extractedModel, annotationTypesToCheck, rootTypeElement, ancestor);

                    for (Element ancestorEnclosedElement : ancestor.getEnclosedElements()) {
                        ElementKind enclosedKind = ancestorEnclosedElement.getKind();
                        if (enclosedKind == ElementKind.FIELD || enclosedKind == ElementKind.METHOD) {
                            extractAnnotations(extractedModel, annotationTypesToCheck, rootTypeElement, ancestorEnclosedElement);


                        }
                    }
                }
            }
        }
    }

    private void extractAnnotations(AnnotationElementsHolder extractedModel, Set<String> annotationTypesToCheck, TypeElement rootTypeElement, Element ancestorEnclosedElement) {
        List<? extends AnnotationMirror> ancestorEnclosedElementAnnotations = ancestorEnclosedElement.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : ancestorEnclosedElementAnnotations) {
            DeclaredType annotationType = annotationMirror.getAnnotationType();
            if (annotationTypesToCheck.contains(annotationType.toString())) {
                TypeElement annotation = (TypeElement) annotationType.asElement();

				/*
                 * rootTypeElement is one of the types that are being compiled
				 * 
				 * ancestorEnclosedElement is the annotated element in an
				 * ancestor of rootTypeElement
				 * 
				 * annotation is a type representing the annotation on
				 * ancestorEnclosedElement
				 */

                mMessager.printMessage(Diagnostic.Kind.NOTE
                        , "DeclaredType:" + annotationType.toString());


                //获取注解相关信息,主要是针对类型转换
                Map<? extends ExecutableElement, ? extends AnnotationValue>
                        map =
                        annotationMirror.getElementValues();

                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : map.entrySet()) {

                    mMessager.printMessage(Diagnostic.Kind.NOTE
                            , "类型转换valid:" + entry.getValue().toString()
                                    +   "--" +     entry.getValue().getValue()+"..."
                    );
                }



                extractedModel.putAncestorAnnotatedElement(annotation.getQualifiedName().toString(), ancestorEnclosedElement, rootTypeElement);


                mMessager.printMessage(Diagnostic.Kind.NOTE
                        , "valid:基类是= " + annotation.getQualifiedName().toString()
                        + " " + ancestorEnclosedElement.getSimpleName().toString()
                        + " ---子类是:" + rootTypeElement.getQualifiedName().toString() +"...");



            }
        }
    }

    /**
     * Finds superclasses until reaching the Object class
     */
    private void addAncestorsElements(Set<TypeElement> elements, TypeElement typeElement) {
        TypeMirror ancestorTypeMirror = typeElement.getSuperclass();

        if (!isRootObjectClass(ancestorTypeMirror) && ancestorTypeMirror instanceof DeclaredType) {
            DeclaredType ancestorDeclaredType = (DeclaredType) ancestorTypeMirror;
            Element ancestorElement = ancestorDeclaredType.asElement();
            if (ancestorElement instanceof TypeElement) {
                elements.add((TypeElement) ancestorElement);
                addAncestorsElements(elements, (TypeElement) ancestorElement);
            }
        }
    }

    private boolean isRootObjectClass(TypeMirror typeMirror) {
        return typeMirror.getKind() == TypeKind.NONE;
    }


    /**
     * 提取根元素注解信息
     *
     * @param annotations
     * @param roundEnvironment
     * @param extractedModel
     */
    private void extractRootElementsAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment, AnnotationElementsHolder extractedModel) {
        for (TypeElement annotation : annotations) {
            extractedModel.putRootAnnotatedElements(annotation.getQualifiedName().toString(), roundEnvironment.getElementsAnnotatedWith(annotation));


            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotation);

            mMessager.printMessage(Diagnostic.Kind.NOTE
                    , annotation.getQualifiedName().toString()
                    + " --" + annotation.getSimpleName().toString()+"]");



        }
    }

}
