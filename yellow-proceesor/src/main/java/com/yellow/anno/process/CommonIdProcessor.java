package com.yellow.anno.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.file.BaseFileObject;
import com.yellow.anno.MyId;
import com.yellow.anno.constant.Constant;
import com.yellow.anno.constant.Constant.ProjectType;

public class CommonIdProcessor {
    
    private static ProjectType projectType;
    
    /**
     * 环境
     */
    private static RoundEnvironment roundEnv;
    
    /**
     * 写文件
     */
    private static Filer mFiler;
    
    /**
     * 提供的工具类
     */
    private static Elements elementUtils;
    
    public static void init(RoundEnvironment roundEnv, Filer mFiler, Elements elementUtils) {
        CommonIdProcessor.roundEnv = roundEnv;
        CommonIdProcessor.mFiler = mFiler;
        CommonIdProcessor.elementUtils = elementUtils;
    }

    public static void process(ProjectType projectType) {
        CommonIdProcessor.projectType = projectType;
        
        Map<BaseFileObject, String> javaFileObjectPackageNameMap = new HashMap<>();

        System.out.println("需要生成相应接口的类有：");
        
        // 获取使用了注解@MyId 的元素
        Set<? extends Element> genElements = roundEnv.getElementsAnnotatedWith(MyId.class);
        for (Element e : genElements) {
            // 现在注解限制了只能写在 字段上，所以这里的 Element类型是确定的
            // e 是 Symbol$VarSymbol ,它的上级,owner, 是 Symbol$ClassSymbol
            
            System.out.println("字段名是 >>> " + e.getSimpleName());
            System.out.println("字段类型是 >>> " + e.asType().toString());

            VarSymbol fieldElement = (VarSymbol) e;

            ClassSymbol javaElement = (ClassSymbol) fieldElement.location();

            BaseFileObject javaFileObject = (BaseFileObject) javaElement.classfile;

            System.out.println("字段所在文件是 >>> " + javaFileObject.toUri());

            
            String packageName = elementUtils.getPackageOf(e).getQualifiedName().toString();
            System.out.println(packageName);
            
            javaFileObjectPackageNameMap.put(javaFileObject, packageName);

        }
        
        javaFileObjectPackageNameMap.forEach( (javaFileObject, packageName) -> {
            try {

                InputStreamReader openReader = (InputStreamReader) javaFileObject.openReader(false);

                URI uri = javaFileObject.toUri();

                String path = replaceFileName(uri.getPath());

                System.out.println(String.format("原始文件是%s, 输出文件为 %s", uri.getPath(), path));

                ArrayList<String> fileContent = getFileContent(openReader);

                handlerFileContent(fileContent);

                writeFileContent(packageName, fileContent);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

    }

    /**
     * <br>读取 原始文件内容
     *
     * @param openReader
     * @return
     * @author YellowTail
     * @since 2019-02-21
     */
    private static ArrayList<String> getFileContent(InputStreamReader openReader) {
        // openInputStream.re

        ArrayList<String> arrayList = new ArrayList<>();

        BufferedReader bReader = null;

        try {
            bReader = new BufferedReader(openReader);
            String str;
            // 按行读取字符串
            while ((str = bReader.readLine()) != null) {
                arrayList.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return arrayList;
    }

    /**
     * <br>处理原始文件内容
     * <br>主要是进行替换 import 注解 等
     *
     * @param arrayList
     * @author YellowTail
     * @since 2019-02-21
     */
    private static void handlerFileContent(ArrayList<String> arrayList) {
        
        Constant.Document importInstance;
        Constant.Document annotationInstance;
        
        if (Constant.ProjectType.B == projectType) {
            importInstance = Constant.Document.BImport;
            annotationInstance = Constant.Document.BAnnotation;
        } else {
            importInstance = Constant.Document.CImport;
            annotationInstance = Constant.Document.CAnnotation;
        }

        for (int i = 0; i < arrayList.size(); i++) {
            String string = arrayList.get(i);

            // 替换 import
            // 可以直接使用 equlas, 没有换行符，因为是使用 BufferedReader readLine 读出来的
            if (string.startsWith("import") && importInstance.getBefore().equals(string.trim())) {
                string = importInstance.getAfter();
            }

            // 替换注解
            if (string.trim().startsWith("@") && annotationInstance.getBefore().equals(string.trim())) {
                string = string.replace(
                        annotationInstance.getBefore(),
                        annotationInstance.getAfter());

            }

            // 替换 class name 和构造方法
            if (string.trim().startsWith("public")) {
                if (-1 != string.indexOf(Constant.Document.FileName.getBefore())) {
                    string = replaceFileName(string);
                }
            }

            //因为没有换行符，所以需要加上，不然整个文件就只有一行
            string += "\r\n";
            arrayList.set(i, string);
        }
    }

    /**
     * <br>把处理好的数据写入到新文件
     * <br>注意：如果自行写文件，那么文件的路径需要为 \target\generated-sources\annotations
     * <br>也可以用 mFiler， 这个会自动写到 这个路径下
     *
     * @param packageName
     * @param arrayList
     * @author YellowTail
     * @since 2019-02-21
     */
    private static void writeFileContent(String packageName, ArrayList<String> arrayList) {
        
        StringBuilder sb = new StringBuilder();
        arrayList.forEach(sb::append);
        
        try {
            JavaFileObject jfo = mFiler.createSourceFile(packageName + "." + Constant.Document.FileName.getAfter(), new Element[]{});
            Writer writer = jfo.openWriter();
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * <br>把入参里的文件名 替换一下
     *
     * @param path
     * @return
     * @author YellowTail
     * @since 2019-02-21
     */
    private static String replaceFileName(String path) {
        return path.replace(Constant.Document.FileName.getBefore(), Constant.Document.FileName.getAfter());
    }
}
