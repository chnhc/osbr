package com.kkIntegration.processor.word;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.kkIntegration.common.utils.FileUtils;
import com.kkIntegration.common.utils.StringUnicodeUtil;
import com.kkIntegration.processor.word.annotation.WordAnnotation;
import com.kkIntegration.processor.word.annotation.WordOneAnnotation;
import com.kkIntegration.processor.word.annotation.WordParamAnnotation;
import com.kkIntegration.processor.word.entity.WordEntity;
import com.kkIntegration.processor.word.entity.WordOneEntiry;
import com.kkIntegration.processor.word.entity.WordParamEntity;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * description:
 * author: ckk
 * create: 2020-03-04 10:30
 */
//  声明处理的 注解 注意只能用一次
@SupportedAnnotationTypes("com.kkIntegration.processor.word.annotation.WordAnnotation")
//  jdk8 以上
@SupportedSourceVersion(SourceVersion.RELEASE_8)
//  自动添加配置文件
@AutoService(Processor.class)
public class WordProcessor extends AbstractProcessor {
    private JavacTrees trees;

    private static Gson gson = new Gson();
    // json 文档输出路径
    private String jsonFile = "f:\\test.json";
    // log输出
    private Messager messager;
    // element转 JCTree 工具
    private JavacElements elementUtils;
    // 文档实体
    private List<WordEntity> wordEntities = new ArrayList<>();
    // 注解参数名
    private String WordParamIdent = WordParamAnnotation.class.getSimpleName();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.trees = JavacTrees.instance(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager = processingEnv.getMessager();
        // element转 JCTree 工具
        elementUtils = (JavacElements) processingEnv.getElementUtils();

        // log
        messager.printMessage(Diagnostic.Kind.NOTE, " 文档生成  ---- ");
        // 获取 环境中 所有 @WordAnnotation 的元素
        for (Element element : roundEnv.getElementsAnnotatedWith(WordAnnotation.class)) {
            // 一个controller
            WordEntity wordEntity = new WordEntity();
            // 分类标签名
            WordAnnotation wordAnnotation =  element.getAnnotation(WordAnnotation.class);
            //messager.printMessage(Diagnostic.Kind.NOTE, wordAnnotation.title() );
            wordEntity.setName(wordAnnotation.title());
            // 父路径
            String parentUrl = parentUrl(element);
            if(parentUrl == null || parentUrl.trim().length()<=0){
                continue;
            }

            // 获取里面的方法 - 子接口集
            element.getEnclosedElements().forEach(e -> {
                WordOneEntiry son= getWordOneEntity(e,parentUrl);
                // 追加子接口
                if(son != null){
                    wordEntity.getSonWords().add(son);
                }

            });

            // 添加到实体中
            wordEntities.add(wordEntity);
        }

        // 持久化
        FileUtils.writeFile(jsonFile,gson.toJson(wordEntities),false );

        return true;
    }



    // 获取父路径
    private String parentUrl(Element element){
        String url = null;
        JCTree.JCClassDecl jcClassDecl = (JCTree.JCClassDecl) elementUtils.getTree(element);
        // 获取类上面的注解
        List<JCTree.JCAnnotation> classAnnos = jcClassDecl.mods.getAnnotations();
        // 注解个数不为空
        if( classAnnos != null && classAnnos.size()>0){
            for(JCTree.JCAnnotation annotation: classAnnos){
                // {@link RequestMapping }
                if("RequestMapping".equals(annotation.annotationType.toString())){
                    List<JCTree.JCExpression> expressions = annotation.args;
                    if(expressions!=null && expressions.size()>0) {
                        for (JCTree.JCExpression expression : expressions) {
                            // 值
                            String val = expression.toString().split("\"")[1];
                            // 名
                            String na = expression.toString().split("=")[0].trim();
                            // 名必须是value ,则是路径
                            if("value".equals(na)){
                                //messager.printMessage(Diagnostic.Kind.NOTE, StringUnicodeUtil.unicodeStr2String(val));
                                url = StringUnicodeUtil.unicodeStr2String(val);
                                break;
                            }
                        }
                    }
                }
                if(url!=null && url.trim().length()>0){
                    break;
                }
            }
        }
        return url;
    }


    // 获取一个子接口
    private WordOneEntiry getWordOneEntity(Element e, String parentUrl) {



        // 有注解 WordOneAnnotation ,才能生成文档
        if(e.getKind() == ElementKind.METHOD && e.getAnnotation(WordOneAnnotation.class) != null){

            WordOneEntiry wordOneEntiry = new WordOneEntiry();

            // 获取子路径
            GetMapping getMapping = e.getAnnotation(GetMapping.class);
            //messager.printMessage(Diagnostic.Kind.NOTE, getMapping.value()[0] +"--------- ");
            String url = parentUrl+getMapping.value()[0];
            // 注解： WordOneAnnotation
            WordOneAnnotation wordOneAnnotation = e.getAnnotation(WordOneAnnotation.class);

            try {
                wordOneEntiry.setMethod(String.valueOf(wordOneAnnotation.method()));
            } catch (MirroredTypeException etype) {
                wordOneEntiry.setMethod(etype.getTypeMirror().toString());
            }
            try {
                wordOneEntiry.setTitle(String.valueOf(wordOneAnnotation.title()));
            } catch (MirroredTypeException etype) {
                wordOneEntiry.setTitle(etype.getTypeMirror().toString());
            }
            try {
                wordOneEntiry.setUrl(url);
            } catch (MirroredTypeException etype) {
                wordOneEntiry.setUrl(etype.getTypeMirror().toString());
            }
            try {
                wordOneEntiry.setDesc(String.valueOf(wordOneAnnotation.desc()));
            } catch (MirroredTypeException etype) {
                wordOneEntiry.setDesc(etype.getTypeMirror().toString());
            }
            try {
                wordOneEntiry.setResponse(wordOneAnnotation.response().getSimpleName());
            } catch (MirroredTypeException etype) {
                wordOneEntiry.setResponse(etype.getTypeMirror().toString());
            }

            // 填充数据
            wordOneEntiry.setParams(getWordParamEntity(e));

            return wordOneEntiry;
        }

        return null;

    }

    // 参数
    private List<WordParamEntity> getWordParamEntity(Element e) {

        List<WordParamEntity> params = new ArrayList<>();

        // 获取方法里的参数
        JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) elementUtils.getTree(e);
        List<JCTree.JCVariableDecl> jcVariableDecls = jcMethodDecl.getParameters();
        if(jcVariableDecls != null && jcVariableDecls.size()>0){
            // 多个参数
            for(JCTree.JCVariableDecl var: jcVariableDecls){
                WordParamEntity one = new WordParamEntity();
                // 参数里有注解
                List<JCTree.JCAnnotation> annotations1= var.mods.annotations;
                if( annotations1 != null && annotations1.size()>0){
                    for(JCTree.JCAnnotation annotation: annotations1){
                        // 注解是 WordParamAnnotation
                        if(WordParamIdent.equals(annotation.annotationType.toString())){
                            List<JCTree.JCExpression> expressions = annotation.args;
                            for(JCTree.JCExpression expression:expressions){
                                String val = expression.toString().split("\"")[1];
                                String na = expression.toString().split("=")[0].trim();
                                //messager.printMessage(Diagnostic.Kind.NOTE, StringUnicodeUtil.unicodeStr2String(val));
                                if("defaultVal".equals(na)){
                                    one.setDefaultVal(StringUnicodeUtil.unicodeStr2String(val));
                                }else if("desc".equals(na)){
                                    one.setDesc(StringUnicodeUtil.unicodeStr2String(val));
                                }
                            }
                        }else if(RequestParam.class.getSimpleName().equals(annotation.annotationType.toString())){
                            // 获取参数名称
                            List<JCTree.JCExpression> expressions = annotation.args;
                            for(JCTree.JCExpression expression:expressions){
                                String val = expression.toString().split("\"")[1];
                                String na = expression.toString().split("=")[0].trim();
                                //messager.printMessage(Diagnostic.Kind.NOTE, StringUnicodeUtil.unicodeStr2String(val));
                                if("value".equals(na)){
                                    one.setName(StringUnicodeUtil.unicodeStr2String(val));
                                }
                            }

                        }
                    }
                }
                if(one.getName() == null || one.getName().length()<=0){
                    // 变量名做参数名称
                    one.setName(var.name.toString());
                }
                // 参数类型
                one.setType(var.vartype.toString());
                // 追加
                params.add(one);
            }
        }

        return params;

    }


}
