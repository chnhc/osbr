package com.kkIntegration.processor.mysql;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.FileSystemResource;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.util.*;

/**
 * 数据源名称 接口常量动态生成器
 * description: ParamTable 注解处理器
 * author: ckk
 * create: 2019-06-03 09:50
 */
//  声明处理的 注解 注意只能用一次
@SupportedAnnotationTypes("com.kkIntegration.processor.mysql.SourceAnnotation")
//  jdk8 以上
@SupportedSourceVersion(SourceVersion.RELEASE_8)
//  自动添加配置文件
@AutoService(Processor.class)
public class SourceProcessor extends AbstractProcessor {

    //包名的前缀
    private static final String PACKAGE = "dataSource";
    // 属性前缀
    private static String prefix = "kkincegration.jta.atomikos";
    // 配置文件所在的 modules
    private static String ymlInModule = "Main";
    // 当前 module 名称
    private static String currentModule = "Processor";
    // 多数据源配置文件名
    private static String ymlName = "application-xa.yml";
    // 当前类路径
    private static String currentPath ="com/kkIntegration/processor/mysql/";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {

        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.WARNING, " 数据源处理  ---- ");
        Map<String, List<String>> sourceNames =  getSourceNames(messager);
        if(sourceNames != null && sourceNames.size() >0 ){
            for (TypeElement typeElement : annotations) {
                for (Element e : env.getElementsAnnotatedWith(typeElement)) {
                    //包裹注解元素的元素, 也就是其父元素, 比如注解了成员变量或者成员函数, 其上层就是该类
                    Element enclosingElement = e.getEnclosingElement();
                    //获取父元素的全类名,用来生成报名
                    String enclosingQualifiedname;
                    if (enclosingElement instanceof PackageElement) {
                        enclosingQualifiedname = ((PackageElement) enclosingElement).getQualifiedName().toString();
                    } else {
                        enclosingQualifiedname = ((TypeElement) enclosingElement).getQualifiedName().toString();
                    }

                    //生成包名
                    String generatePackageName = enclosingQualifiedname.substring(0, enclosingQualifiedname.lastIndexOf(".")) + "." + PACKAGE;

                    try {
                        for(Map.Entry<String, List<String>> s : sourceNames.entrySet()){
                            // 生成类包名，类名
                            //获取元素名并将其首字母大写
                            String name = s.getKey();
                            char c = Character.toUpperCase(name.charAt(0));
                            name = String.valueOf(c + name.substring(1));

                            JavaFile javaFile = JavaFile.builder(generatePackageName, generateClass(name, s.getValue())).
                                    build();
                            javaFile.writeTo(processingEnv.getFiler());
                        }
                    } catch (Exception er) {
                        er.printStackTrace();

                    }
                }
            }
        }
        return true;
    }

    // 创建接口类
    public static TypeSpec generateClass(String classes, List<String> sourceNames) {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(classes)
                .addModifiers(Modifier.PUBLIC);

        // 循环添加字段
        for(String s: sourceNames){
            builder.addField(makeCreateField(s, s));
        }

        return builder.build();
    }


    // 创建字段
    private static FieldSpec makeCreateField(String fieldName, String value) {
        return FieldSpec.builder(String.class, fieldName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", value)
                .build();

    }

    // 获取配置中 yml 里所有的 sourceName
    private  Map<String, List<String>> getSourceNames(Messager messager){
        // 解析yml
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        // 获取路径，解析指定yml
        String f= this.getClass().getResource("").getPath()
                .replace(currentPath,"")
                .replace(currentModule,ymlInModule)+ ymlName;

        if(new File(f).exists()){
            // 解析
            yaml.setResources(new FileSystemResource(f) );
            // 获取属性
            Properties propertySource=yaml.getObject();
            // 所有注解名
            Map<String, List<String>> classNames= new HashMap<>();
            int i = 0;
            while ((propertySource.getProperty(prefix+"."+"datasourceList["+i+"].beanName") !=null)){
                int u = 0;
                List<String> className = new ArrayList<>();
                while ((propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.urls["+u+"].url") !=null)){
                    className.add((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.urls["+u+"].sourceName"));
                    messager.printMessage(Diagnostic.Kind.WARNING, "Printing: " + (String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.urls["+u+"].sourceName"));
                    u++;
                }
                if(className.size() > 0){
                    classNames.put(propertySource.getProperty(prefix+"."+"datasourceList["+i+"].beanName"), className);
                }

                i++;
            }

            return classNames;
        }

        return null;

    }




}