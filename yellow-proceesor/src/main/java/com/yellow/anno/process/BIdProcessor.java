package com.yellow.anno.process;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import com.yellow.anno.constant.Constant;

@SupportedAnnotationTypes("com.yellow.anno.MyId")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BIdProcessor extends AbstractProcessor {

    private Filer mFiler;
    
    private Elements elementUtils;         //工具类

    private int r = 1;// 轮循次数

    public BIdProcessor() {
        super();
        System.out.println("bbbbbbbbbbb222222");
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // 初始化Filer和Messager
        this.mFiler = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
        
        this.elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 目的是：
        // 扫描 MyId 注解，生成B端可用的 类
        // C端代码的生成在另外一个 处理器内
        
        CommonIdProcessor.init(roundEnv, mFiler, elementUtils);
        
        CommonIdProcessor.process(Constant.ProjectType.B);
        
        System.out.println("-------------------注解处理器第" + (r++) + "次循环处理结束...\n");

        return false;
    }
    
}
