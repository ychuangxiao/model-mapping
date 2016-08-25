
package com.banditcat.android.transformer.compiler.internal;



import com.banditcat.android.transformer.annotation.FiledByCollection;
import com.banditcat.android.transformer.annotation.Filed;
import com.banditcat.android.transformer.annotation.MappedClass;
import com.banditcat.android.transformer.annotation.Parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;


public class AnnotationsProcessor   {

    final String BOOLEAN_FIELD_PREFIX = "is";
    final String SOME_FIELD_PREFIX = "get";

    RoundEnvironment roundEnvironment;
    Map<String, MapperInfo> mappersList;

    Messager mMessager;
    ProcessingEnvironment processingEnv;

    public boolean process(ProcessingEnvironment processingEnv,Messager messager,Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnvironment = roundEnv;
        mappersList = new HashMap<>();
        this.processingEnv = processingEnv;
        mMessager = messager;

        processMappedClassAnnotationElements();
        processMappedFiledAnnotationElements();
        processSupperMappedFiledAnnotationElements();

        processParseAnnotationElements();

        //processCollectionFiledAnnotationElements();

        buildMapperObjects();
        generateTransformersJavaFiles();

        mMessager.printMessage(Diagnostic.Kind.MANDATORY_WARNING,"我靠3");
        return true;
    }


    /**
     * 处理基类字段信息
     */
    void processSupperMappedFiledAnnotationElements() {

        Element supperElement = null;

        ClassInfo classInfo = null;

        TypeElement supperTypeElement = null;
        for (Element mappableElement : roundEnvironment.getElementsAnnotatedWith(MappedClass.class)) {


            if (mappableElement.getKind() != ElementKind.CLASS) {
                continue;
            }


            TypeMirror typeMirror = mappableElement.asType();
            Types TypeUtils = processingEnv.getTypeUtils();


            supperTypeElement = (TypeElement) TypeUtils.asElement(typeMirror);


            //判断当前类的基类是不是object

            if (supperTypeElement.getSuperclass().toString().equals("java.lang.Object")) {
                continue;
            }

            //得到当前类的基类名称
            String supperName = supperTypeElement.getSuperclass().toString();


            //得到基类的信息

            for (Element element : roundEnvironment.getElementsAnnotatedWith(MappedClass.class)) {
                classInfo = extractClassInformation(element);

                if (supperName.equals(classInfo.getFullName())) {


                    supperElement = element;
                    break;
                }
            }


            if (null == supperElement) {
                continue;
            }


            for (Element filedElement : roundEnvironment.getElementsAnnotatedWith(Filed.class)) {
                if (filedElement.getKind() != ElementKind.FIELD) {
                    continue;
                }

                classInfo = extractClassInformationFromField(filedElement);


                if (classInfo.getFullName().equals(extractClassInformation(supperElement).getFullName())) {


                    if (filedElement.getKind() != ElementKind.FIELD) {
                        continue;
                    }


                    Filed filedAnnotation = filedElement.getAnnotation(Filed.class);

                    String fieldName = filedElement.getSimpleName().toString();
                    String fieldType = filedElement.asType().toString();


                    boolean isPublicField = filedElement.getModifiers().contains(Modifier.PUBLIC);
                    String toFieldName = filedAnnotation.toField();

                    MapperFieldInfo mappingFieldInfo = new MapperFieldInfo(fieldName, fieldType, toFieldName, isPublicField);


                    ClassInfo classInfo2 = extractClassInformation(mappableElement);


                    getMapper(classInfo2)
                            .getFields()
                            .add(mappingFieldInfo);

                    processSupperParseAnnotationElements(supperTypeElement, filedElement);
                }
            }


        }
    }

    private void buildMapperObjects() {

        HashMap<String, String> hashMap = new HashMap<>();
        for (MapperInfo mapper : this.mappersList.values()) {
            Collection<String> mapperImports = new ArrayList<>();
            Collection<String> classVars = new ArrayList<>();
            Collection<String> directFields = new ArrayList<>();
            Collection<String> inverseFields = new ArrayList<>();

            mapperImports.add("import java.util.ArrayList;");
            mapperImports.add("import java.util.Collection;");
            mapperImports.add(String.format(Tools.IMPORT_PATTERN, mapper.packageName, mapper.className));
            mapperImports.add(String.format(Tools.IMPORT_PATTERN, mapper.linkedPackageName, mapper.linkedClassName));

            hashMap.clear();
            for (MapperFieldInfo mapperField : mapper.getFields()) {


                String originFieldName = mapperField.fieldName;
                String destinationFieldName = mapperField.fieldName;

                if (mapperField.withFieldName != null && !mapperField.withFieldName.trim().equals(""))
                    destinationFieldName = mapperField.withFieldName;

                if (!mapperField.isPublicField) {
                    destinationFieldName = toUpperCamelCase(destinationFieldName);
                    originFieldName = toUpperCamelCase(originFieldName);
                }

                if (mapperField.originToDestinationParserClassName == null && mapperField.destinationToOriginParserClassName == null) {
                    MapperInfo mapperInfo = mapperForMapperField(mapperField);
                    if (mapperInfo != null) {
                        //防止引入多次包
                        if (!hashMap.containsKey(mapperInfo.mapperClassName)) {
                            mapperImports.add(String.format(Tools.IMPORT_PATTERN, mapperInfo.mapperPackageName, mapperInfo.mapperClassName));
                            hashMap.put(mapperInfo.mapperClassName, mapperInfo.mapperClassName);
                        }

                        classVars.add(String.format(Tools.MAPPER_CLASS_VAR_CONSTANT_PATTERN, mapperInfo.mapperClassName, toLowerCamelCase(mapperInfo.mapperClassName), mapperInfo.mapperClassName));

                        String mapperCompositePattern = getMapperCompositePattern(mapperField);
                        directFields.add(String.format(mapperCompositePattern, destinationFieldName, toLowerCamelCase(mapperInfo.mapperClassName), returnedFieldPrefix(mapperField, originFieldName)));
                        inverseFields.add(String.format(mapperCompositePattern, originFieldName, toLowerCamelCase(mapperInfo.mapperClassName), returnedFieldPrefix(mapperField, destinationFieldName)));
                    } else {
                        String mapperFieldPattern = getMapperFieldPattern(mapperField);
                        directFields.add(String.format(mapperFieldPattern, destinationFieldName, returnedFieldPrefix(mapperField, originFieldName)));
                        inverseFields.add(String.format(mapperFieldPattern, originFieldName, returnedFieldPrefix(mapperField, destinationFieldName)));
                    }
                } else {

                    //防止引入多次包
                    if (!hashMap.containsKey(mapperField.originToDestinationParserClassName)) {
                        mapperImports.add(String.format(Tools.IMPORT_PATTERN, mapperField.originToDestinationParserPackageName, mapperField.originToDestinationParserClassName));
                        hashMap.put(mapperField.originToDestinationParserClassName, mapperField.originToDestinationParserClassName);
                    }

                    //防止引入多次包
                    if (!hashMap.containsKey(mapperField.destinationToOriginParserClassName)) {
                        mapperImports.add(String.format(Tools.IMPORT_PATTERN, mapperField.destinationToOriginParserPackageName, mapperField.destinationToOriginParserClassName));
                        hashMap.put(mapperField.destinationToOriginParserClassName, mapperField.destinationToOriginParserClassName);
                    }

                    String mapperFieldWithParserPattern = getMapperFieldWithParserPattern(mapperField);
                    directFields.add(String.format(mapperFieldWithParserPattern, destinationFieldName, mapperField.originToDestinationParserClassName, returnedFieldPrefix(mapperField, originFieldName)));
                    inverseFields.add(String.format(mapperFieldWithParserPattern, originFieldName, mapperField.destinationToOriginParserClassName, returnedFieldPrefix(mapperField, destinationFieldName)));
                }
            }


            if (null != mapper.getMapperCollectionInfoList()) {
                for (MapperCollectionInfo mapperCollectionInfo : mapper.getMapperCollectionInfoList()) {
                    //防止引入多次包
                    if (!hashMap.containsKey(mapperCollectionInfo.linkedClass.getFullName())) {
                        mapperImports.add(String.format(Tools.IMPORT_PATTERN, mapperCollectionInfo.linkedClass.packageName, mapperCollectionInfo.linkedClass.className));
                        hashMap.put(mapperCollectionInfo.linkedClass.getFullName(), mapperCollectionInfo.linkedClass.getFullName());
                    }

                    //防止引入多次包
                    if (!hashMap.containsKey(mapperCollectionInfo.mapperClass.getFullName())) {
                        mapperImports.add(String.format(Tools.IMPORT_PATTERN, mapperCollectionInfo.mapperClass.packageName, mapperCollectionInfo.mapperClass.className));
                        hashMap.put(mapperCollectionInfo.mapperClass.getFullName(), mapperCollectionInfo.mapperClass.getFullName());
                    }
                }
            }


            generateMapperJavaFile(mapper, classVars, mapperImports, directFields, inverseFields);
        }
    }

    private String getMapperCompositePattern(MapperFieldInfo mapperField) {
        String result;

        if (mapperField.isPublicField) {
            result = Tools.MAPPER_FIELD_COMPOSITE_PATTERN;
        } else {
            result = Tools.MAPPER_STANDARD_FIELD_COMPOSITE_PATTERN;
        }

        return result;
    }

    private String getMapperFieldWithParserPattern(MapperFieldInfo mapperField) {
        String result;

        if (mapperField.isPublicField) {
            result = Tools.MAPPER_FIELD_WITH_PARSER_PATTERN;
        } else {
            result = Tools.MAPPER_STANDARD_FIELD_WITH_PARSER_PATTERN;
        }

        return result;
    }

    private String getMapperFieldPattern(MapperFieldInfo mapperField) {
        String result;

        if (mapperField.isPublicField) {
            result = Tools.MAPPER_FIELD_PATTERN;
        } else {
            result = Tools.MAPPER_STANDARD_FIELD_PATTERN;
        }

        return result;
    }

    private MapperInfo mapperForMapperField(MapperFieldInfo mapperField) {
        for (MapperInfo mapperInfo : mappersList.values()) {
            if (mapperField.fieldType.equals(mapperInfo.mappableClassName)) {
                return mapperInfo;
            }
        }
        return null;
    }

    private String toLowerCamelCase(String className) {
        return className.substring(0, 1).toLowerCase().concat(className.substring(1));
    }

    private String toUpperCamelCase(String fielName) {
        return fielName.substring(0, 1).toUpperCase().concat(fielName.substring(1));
    }

    private String returnedFieldPrefix(MapperFieldInfo mapperField, String fieldName) {
        if (mapperField.isPublicField)
            return fieldName;

        return returnedFieldPrefix(mapperField.fieldType, fieldName);
    }

    private String returnedFieldPrefix(String fieldType, String fieldName) {
        String result;

        if (fieldType.equals("boolean")) {
            if (fieldName.toLowerCase().startsWith(BOOLEAN_FIELD_PREFIX)) {
                result = toLowerCamelCase(fieldName);
            } else {
                result = BOOLEAN_FIELD_PREFIX.concat(fieldName);
            }
        } else {
            result = SOME_FIELD_PREFIX.concat(fieldName);
        }

        return result;
    }

    private void generateMapperJavaFile(MapperInfo mapper, Collection<String> classVars, Collection<String> imports, Collection<String> directFields, Collection<String> inverseFields) {

        try {

            String mapperCanonicalName = String.format("%s.%s", mapper.mapperPackageName, mapper.mapperClassName);
            writeTrace(String.format("Generating source file for Mapper with name %s", mapperCanonicalName));

            JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(mapperCanonicalName);
            BufferedWriter buffer = new BufferedWriter(javaFileObject.openWriter());

            buffer.append(String.format(Tools.PACKAGE_PATTERN, mapper.mapperPackageName));
            buffer.newLine();

            for (String classImport : imports) {
                buffer.newLine();
                buffer.append(classImport);
            }

            buffer.newLine();
            buffer.newLine();
            buffer.append(String.format(Tools.CLASS_PATTERN, mapper.mapperClassName));

            if (classVars.size() > 0) {
                buffer.newLine();
                for (String classVar : classVars) {
                    buffer.newLine();
                    buffer.append("\t").append(classVar);
                }
            }

            generateTransformMethod(buffer, mapper.className, mapper.linkedClassName, directFields);
            generateTransformMethod(buffer, mapper.linkedClassName, mapper.className, inverseFields);


            //处理集合

            if (null != mapper.getMapperCollectionInfoList()) {
                for (MapperCollectionInfo mapperCollectionInfo : mapper.getMapperCollectionInfoList())
                {
                    buffer.newLine();
                    buffer.newLine();
                    buffer.append(String.format("\tpublic Collection<%s> transform%sTo%s(Collection<%s> data) {"
                            , mapperCollectionInfo.linkedClass.className
                            , mapperCollectionInfo.mapperClass.className
                            , mapperCollectionInfo.linkedClass.className
                            , mapperCollectionInfo.mapperClass.className
                    ));
                    buffer.newLine();

                    buffer.newLine();
                    buffer.newLine();
                    buffer.append("\t\treturn null;");
                    buffer.newLine();
                    buffer.append("\t}");

                    buffer.newLine();
                    buffer.newLine();
                    buffer.append(String.format("\tpublic Collection<%s> transform%sTo%s(Collection<%s> data) {"
                            , mapperCollectionInfo.mapperClass.className
                            , mapperCollectionInfo.linkedClass.className
                            , mapperCollectionInfo.mapperClass.className
                            , mapperCollectionInfo.linkedClass.className
                    ));
                    buffer.newLine();

                    buffer.newLine();
                    buffer.newLine();
                    buffer.append("\t\treturn null;");
                    buffer.newLine();
                    buffer.append("\t}");


                }
            }


            buffer.newLine();
            buffer.append("}");
            buffer.close();

        } catch (IOException error) {
            throw new RuntimeException(error);
        }
    }


    private void generateCodes(String message) {
        File dir = new File("/Users/banditcat/Work/Test");

        if (dir.exists()) {
            dir.delete();
        }
        if (!dir.exists())
            dir.mkdirs();
        // 创建文件


        File file = new File(dir, "processor.txt");
        try {
            /**
             * 编写json文件内容
             */


            FileWriter fw = new FileWriter(file, true);

            fw.write(message + "\n\n");
            fw.flush();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateTransformMethod(BufferedWriter buffer, String className, String linkedClassName, Collection<String> fields) throws IOException {
        buffer.newLine();
        buffer.newLine();
        buffer.append(String.format("\tpublic %s transform(%s data) {", linkedClassName, className));
        buffer.newLine();
        buffer.append(String.format("\t\t%s result = null;", linkedClassName));

        buffer.newLine();
        buffer.newLine();
        buffer.append("\t\tif (null == data ) {");
        buffer.newLine();
        buffer.append("\t\t\treturn result;");
        buffer.newLine();
        buffer.append("\t\t}");

        buffer.newLine();
        buffer.append(String.format("\t\tresult = new %s();", linkedClassName));
        buffer.newLine();

        for (String field : fields) {
            buffer.newLine();
            buffer.append(String.format("\t\t%s", field));
        }


        buffer.newLine();
        buffer.newLine();
        buffer.append("\t\treturn result;");
        buffer.newLine();
        buffer.append("\t}");
    }

    private void generateTransformersJavaFiles() {
        Map<String, TransformerInfo> transformersList = new HashMap<>();

        if (mappersList.size() > 0) {
            for (MapperInfo mapper : mappersList.values()) {
                if (!transformersList.containsKey(mapper.packageName)) {
                    String packageName = String.format(Tools.TRANSFORMER_PACKAGE_PATTERN, mapper.packageName);
                    String className = Tools.TRANSFORMER_CLASS_NAME;
                    TransformerInfo transformer = new TransformerInfo(packageName, className);
                    transformersList.put(mapper.packageName, transformer);
                }

                TransformerInfo transformer = transformersList.get(mapper.packageName);
                transformer.getMappers().add(mapper);
            }

            generateTransformerJavaFile(transformersList);
        }
    }

    private void generateTransformerJavaFile(Map<String, TransformerInfo> transformers) {
        try {

            if (transformers.size() > 0) {


                for (TransformerInfo transformer : transformers.values()) {
                    String packageName = transformer.packageName;
                    String className = transformer.className;

                    String transformerCanonicalName = String.format("%s.%s", packageName, className);
                    writeTrace(String.format("Generating source file for Transformer class with name %s", transformerCanonicalName));

                    JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(transformerCanonicalName);
                    BufferedWriter buffer = new BufferedWriter(javaFileObject.openWriter());

                    buffer.append(String.format(Tools.PACKAGE_PATTERN, packageName));
                    buffer.newLine();

                    //region "Class Imports Generation"

                    buffer.newLine();
                    buffer.append(String.format(Tools.IMPORT_PATTERN, "com.banditcat.transformer.internal", "AbstractTransformer"));


                    for (MapperInfo mapper : transformer.getMappers()) {


                        buffer.newLine();
                        buffer.append(String.format(Tools.IMPORT_PATTERN, mapper.mapperPackageName, mapper.mapperClassName));


                    }

                    //endregion

                    //region "Class Generation"

                    buffer.newLine();
                    buffer.newLine();
                    buffer.append(String.format(Tools.TRANSFORMER_CLASS_PATTERN, className));

                    //region "Constructor Generation"

                    buffer.newLine();
                    buffer.append(String.format("\tpublic %s() {", className));
                    buffer.newLine();
                    buffer.append("\t\tsuper();");

                    //region "Variable Inicialization"

                    buffer.newLine();
                    for (MapperInfo mapper : transformer.getMappers()) {
                        buffer.newLine();
                        buffer.append(String.format("\t\taddMapper(\"%s.%s\", new %s());", mapper.packageName, mapper.className, mapper.mapperClassName));
                        buffer.newLine();
                        buffer.append(String.format("\t\taddMapper(\"%s.%s\", new %s());", mapper.linkedPackageName, mapper.linkedClassName, mapper.mapperClassName));
                    }

                    //endregion

                    buffer.newLine();
                    buffer.append("\t}");

                    //endregion

                    buffer.newLine();
                    buffer.append("}");

                    //endregion

                    buffer.close();
                }
            }

        } catch (IOException error) {
            throw new RuntimeException(error);
        }
    }


    /**
     * 处理集合字段
     */
    private void processCollectionFiledAnnotationElements() {


        for (Element mappableElement : roundEnvironment.getElementsAnnotatedWith(FiledByCollection.class)) {


            if (mappableElement.getKind() != ElementKind.FIELD) {
                continue;
            }


            AnnotationMirror mappableAnnotationMirror = getAnnotationMirror(mappableElement, FiledByCollection.class);
            AnnotationValue annotationValue = getAnnotationValue(mappableAnnotationMirror, "with");


            TypeElement linkedElement = getTypeElement(annotationValue);

            ClassInfo mappableClassInfo = extractClassInformationFromField(mappableElement);

            ClassInfo linkedClassInfo = extractClassInformation(linkedElement);


            MapperInfo mapperInfo = getMapper(linkedClassInfo);

            ClassInfo tempClassInfo = new ClassInfo(mapperInfo.linkedPackageName, mapperInfo.linkedClassName);


            //要知道这个属性的类是谁


            generateCodes(mappableClassInfo.getFullName());//当前类
            generateCodes(linkedClassInfo.getFullName());//泛型对象
            generateCodes(tempClassInfo.getFullName());//泛型映射类


            getMapper(mappableClassInfo).getMapperCollectionInfoList().add(new MapperCollectionInfo(


                    mappableClassInfo, linkedClassInfo, tempClassInfo));

        }
    }

    /**
     * 处理类映射
     */
    private void processMappedClassAnnotationElements() {
        for (Element mappableElement : roundEnvironment.getElementsAnnotatedWith(MappedClass.class)) {


            if (mappableElement.getKind() != ElementKind.CLASS) {
                continue;
            }

            AnnotationMirror mappableAnnotationMirror = getAnnotationMirror(mappableElement, MappedClass.class);
            AnnotationValue annotationValue = getAnnotationValue(mappableAnnotationMirror, "with");
            TypeElement linkedElement = getTypeElement(annotationValue);


            ClassInfo mappableClassInfo = extractClassInformation(mappableElement);
            ClassInfo linkedClassInfo = extractClassInformation(linkedElement);

            if (!haveMapper(mappableClassInfo))
                createMapper(mappableElement.asType().toString(), mappableClassInfo, linkedClassInfo);

        }
    }

    /**
     * 处理字段映射
     */
    private void processMappedFiledAnnotationElements() {


        for (Element mappedElement : roundEnvironment.getElementsAnnotatedWith(Filed.class)) {


            if (mappedElement.getKind() != ElementKind.FIELD) {
                continue;
            }


            Filed filedAnnotation = mappedElement.getAnnotation(Filed.class);

            String fieldName = mappedElement.getSimpleName().toString();
            String fieldType = mappedElement.asType().toString();


            boolean isPublicField = mappedElement.getModifiers().contains(Modifier.PUBLIC);
            String toFieldName = filedAnnotation.toField();

            MapperFieldInfo mappingFieldInfo = new MapperFieldInfo(fieldName, fieldType, toFieldName, isPublicField);

            ClassInfo classInfo = extractClassInformationFromField(mappedElement);


            getMapper(classInfo)
                    .getFields()
                    .add(mappingFieldInfo);

        }
    }

    /**
     * @param ownerTypeElement
     */
    private void processSupperParseAnnotationElements(TypeElement ownerTypeElement, Element parseElement) {

        //查看基类是否有字段类型处理


        if (parseElement.getKind() == ElementKind.FIELD) {

            AnnotationMirror parseAnnotationMirror = getAnnotationMirror(parseElement, Parse.class);
            AnnotationValue originToDestinationWithValue = getAnnotationValue(parseAnnotationMirror, "originToDestinationWith");

            if (null == originToDestinationWithValue) {
                return;
            }
            AnnotationValue destinationToOriginWithValue = getAnnotationValue(parseAnnotationMirror, "destinationToOriginWith");

            if (null == destinationToOriginWithValue) {
                return;
            }


            TypeElement originToDestinationValue = getTypeElement(originToDestinationWithValue);
            TypeElement destinationToOriginValue = getTypeElement(destinationToOriginWithValue);

            String fieldName = parseElement.getSimpleName().toString();

            ClassInfo ownerClass = extractClassInformation(ownerTypeElement);


            ClassInfo originToDestinationParserClass = extractClassInformation(originToDestinationValue);
            ClassInfo destinationToOriginParserClass = extractClassInformation(destinationToOriginValue);


            MapperFieldInfo mapperField = getMapper(ownerClass).getField(fieldName);
            if (mapperField != null) {
                mapperField.originToDestinationParserPackageName = originToDestinationParserClass.packageName;
                mapperField.originToDestinationParserClassName = originToDestinationParserClass.className;
                mapperField.destinationToOriginParserPackageName = destinationToOriginParserClass.packageName;
                mapperField.destinationToOriginParserClassName = destinationToOriginParserClass.className;
            } else {
                writeError(String.format("You have configured a @Parse annotation without a @Filed annotation on %s.%s.", ownerClass.getFullName(), fieldName));
            }
        }
    }

    private void processParseAnnotationElements() {
        for (Element parseElement : roundEnvironment.getElementsAnnotatedWith(Parse.class)) {
            if (parseElement.getKind() == ElementKind.FIELD) {

                AnnotationMirror parseAnnotationMirror = getAnnotationMirror(parseElement, Parse.class);
                AnnotationValue originToDestinationWithValue = getAnnotationValue(parseAnnotationMirror, "originToDestinationWith");
                AnnotationValue destinationToOriginWithValue = getAnnotationValue(parseAnnotationMirror, "destinationToOriginWith");
                TypeElement originToDestinationValue = getTypeElement(originToDestinationWithValue);
                TypeElement destinationToOriginValue = getTypeElement(destinationToOriginWithValue);

                String fieldName = parseElement.getSimpleName().toString();

                ClassInfo ownerClass = extractClassInformationFromField(parseElement);
                ClassInfo originToDestinationParserClass = extractClassInformation(originToDestinationValue);
                ClassInfo destinationToOriginParserClass = extractClassInformation(destinationToOriginValue);

                MapperFieldInfo mapperField = getMapper(ownerClass).getField(fieldName);
                if (mapperField != null) {
                    mapperField.originToDestinationParserPackageName = originToDestinationParserClass.packageName;
                    mapperField.originToDestinationParserClassName = originToDestinationParserClass.className;
                    mapperField.destinationToOriginParserPackageName = destinationToOriginParserClass.packageName;
                    mapperField.destinationToOriginParserClassName = destinationToOriginParserClass.className;
                } else {
                    writeError(String.format("You have configured a @Parse annotation without a @Filed annotation on %s.%s.", ownerClass.getFullName(), fieldName));
                }
            }
        }
    }

    private boolean haveMapper(ClassInfo classInfo) {
        String mapperClassFullName = classInfo.getFullName();
        return mappersList.containsKey(mapperClassFullName);
    }

    private MapperInfo createMapper(String mappableClassName
            , ClassInfo classInfo
            , ClassInfo linkedClassInfo) {
        MapperInfo mapper = new MapperInfo(mappableClassName, classInfo.packageName, classInfo.className, linkedClassInfo.packageName, linkedClassInfo.className);
        mappersList.put(mapper.getFullName(), mapper);
        return mapper;
    }

    private MapperInfo getMapper(ClassInfo classInfo) {
        return mappersList.get(classInfo.getFullName());
    }


    /**
     * 获取类信息
     *
     * @param element
     * @return
     */
    private ClassInfo extractClassInformationFromField(Element element) {
        Element classElement = element.getEnclosingElement();


        return extractClassInformation(classElement);
    }

    private ClassInfo extractClassInformation(Element element) {
        PackageElement packageElement = (PackageElement) element.getEnclosingElement();
        String className = element.getSimpleName().toString();
        String packageName = packageElement.getQualifiedName().toString();


        return new ClassInfo(packageName, className);
    }

    private AnnotationMirror getAnnotationMirror(Element element, Class<?> annotationType) {
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

    private AnnotationValue getAnnotationValue(AnnotationMirror annotation, String field) {
        if (annotation != null) {
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotation.getElementValues().entrySet()) {
                if (entry.getKey().getSimpleName().toString().equals(field)) {
                    return entry.getValue();
                }
            }
        }

        return null;
    }

    private TypeElement getTypeElement(AnnotationValue value) {
        TypeElement result = null;

        if (value != null) {
            TypeMirror typeMirror = (TypeMirror) value.getValue();
            Types TypeUtils = processingEnv.getTypeUtils();
            result = (TypeElement) TypeUtils.asElement(typeMirror);
        }

        return result;
    }

    private void writeError(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

    private void writeTrace(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    private class ClassInfo {
        public final String className;
        public final String packageName;

        public ClassInfo(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        public String getFullName() {
            return String.format("%s.%s", packageName, className);
        }

        @Override
        public String toString() {
            return String.format("%s.%s", packageName, className);
        }
    }

    private class TransformerInfo extends ClassInfo {
        private List<MapperInfo> mappers;

        public List<MapperInfo> getMappers() {
            return mappers;
        }

        public TransformerInfo(String packageName, String className) {
            super(packageName, className);
            mappers = new ArrayList<>();
        }
    }


    private class MapperCollectionInfo {

        public final ClassInfo currentClass;
        public final ClassInfo mapperClass;
        public final ClassInfo linkedClass;


        private MapperCollectionInfo(ClassInfo currentClass, ClassInfo mapperClass, ClassInfo linkedClass) {
            this.currentClass = currentClass;
            this.mapperClass = mapperClass;
            this.linkedClass = linkedClass;
        }
    }

    private class MapperInfo extends ClassInfo {
        public final String mapperClassName;
        public final String mapperPackageName;
        public final String linkedClassName;
        public final String linkedPackageName;
        public final String mappableClassName;

        private List<MapperCollectionInfo> mapperCollectionInfoList = new ArrayList<>();

        public List<MapperCollectionInfo> getMapperCollectionInfoList() {
            return mapperCollectionInfoList;
        }


        private List<MapperFieldInfo> mappedFieldsList = new ArrayList<>();

        public List<MapperFieldInfo> getFields() {
            return mappedFieldsList;
        }

        public MapperFieldInfo getField(String fieldName) {
            MapperFieldInfo result = null;
            for (MapperFieldInfo field : mappedFieldsList) {
                if (field.fieldName.equals(fieldName)) {
                    result = field;
                    break;
                }
            }
            return result;
        }

        public MapperInfo(String mappableClassName, String packageName, String className, String linkedPackageName, String linkedClassName) {
            super(packageName, className);

            this.mappableClassName = mappableClassName;
            this.mapperClassName = String.format(Tools.MAPPER_CLASS_NAME_PATTERN, className);
            this.mapperPackageName = String.format(Tools.MAPPER_PACKAGE_PATTERN, packageName);
            this.linkedPackageName = linkedPackageName;
            this.linkedClassName = linkedClassName;
        }
    }

    private class MapperFieldInfo {
        public final String fieldName;
        public final String fieldType;
        public final String withFieldName;
        public final boolean isPublicField;
        public String originToDestinationParserPackageName;
        public String originToDestinationParserClassName;
        public String destinationToOriginParserPackageName;
        public String destinationToOriginParserClassName;

        public MapperFieldInfo(String fieldName, String fieldType, String withFieldName, boolean isPublicField) {
            this.fieldName = fieldName;
            this.fieldType = fieldType;
            this.withFieldName = withFieldName;
            this.isPublicField = isPublicField;
        }
    }
}
