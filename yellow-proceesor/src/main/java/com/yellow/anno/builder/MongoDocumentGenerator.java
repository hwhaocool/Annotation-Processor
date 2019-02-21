package com.yellow.anno.builder;

import java.util.Date;

import javax.lang.model.element.Modifier;

import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.springframework.data.annotation.Id;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * <br>MongoDocument 生成器
 * <br>弃用，代码还是自己来生成，可维护性不高，理想状况是替换
 *
 * @author YellowTail
 * @since 2019-02-20
 */
public class MongoDocumentGenerator {
    
    public static enum ProjectType {
        B, C
        ;
    }
    
    public static enum FieldMethodMapping {
        ID("_id", "_id", String.class),
        ModifyTime("modifyTime", "ModifyTime", Date.class),
        CreateTime("createTime", "CreateTime", Date.class),
        Del_flag_inner("del_flag_inner", "Del_flag_inner", Integer.class),
        ;
        
        private String fieldName;
        private String methodSuffix;
        private Class classType;
        
        private FieldMethodMapping(String fieldName, String methodSuffix, Class classType) {
            this.fieldName = fieldName;
            this.methodSuffix = methodSuffix;
            this.classType = classType;
        }
    }

    public static void generate(ProjectType projectType) {
        //_id
        FieldSpec idField = genId(projectType);
        
        genNormalField("modifyTime", Date.class);
        
        //modifyTime
        FieldSpec modifyTimeField = FieldSpec.builder(Date.class, "modifyTime")
                .addModifiers(Modifier.PROTECTED)
                .build();
        
        //createTime
        FieldSpec createTimeField = FieldSpec.builder(Date.class, "createTime")
                .addModifiers(Modifier.PROTECTED)
                .build();
        
        //del_flag_inner
        FieldSpec delFlagInnerField = FieldSpec.builder(Integer.class, "del_flag_inner")
                .addModifiers(Modifier.PROTECTED)
                .build();
        
        //构造方法
        MethodSpec constructorDefault = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "greeting")
                .addStatement("this.$N = $N", "greeting", "greeting")
                .build();
        
        MethodSpec constructorWithId = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "_id")
                .addStatement("this.$N = $N", "_id", "_id")
                .build();
        
        //get set
        MethodSpec getId = MethodSpec.methodBuilder("get_id")
                .returns(String.class)
                .addStatement("return $N", "_id")
                .build();
        
        MethodSpec setId = MethodSpec.methodBuilder("set_id")
                .addParameter(String.class, "_id")
                .addStatement("this.$N = $N", "_id", "_id")
                .build();
        
        MethodSpec getModifyTime = MethodSpec.methodBuilder("getModifyTime")
                .returns(Date.class)
                .addStatement("return $N", "modifyTime")
                .build();
        
        MethodSpec setModifyTime = MethodSpec.methodBuilder("setModifyTime")
                .addParameter(Date.class, "modifyTime")
                .addStatement("this.$N = $N", "modifyTime", "modifyTime")
                .build();
        
        //文件对象
        TypeSpec helloWorld = TypeSpec.classBuilder("MongoDocument")
                .addModifiers(Modifier.PUBLIC)
                .addField(genId(projectType))
                .addField(genNormalField("modifyTime", Date.class))
                .addField(genNormalField("createTime", Date.class))
                .addField(genNormalField("del_flag_inner", Integer.class))
                
                .addMethod(constructorDefault)
                .addMethod(constructorWithId)
                
                .build();

        JavaFile javaFile = JavaFile.builder("com.xxx.MongoDocument", helloWorld)
            .build();

//        javaFile.writeTo(System.out);
    }
    
    public void gen2String() {
        MethodSpec toString = MethodSpec.methodBuilder("toString")
                .addAnnotation(Override.class)
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $S", "Hoverboard")
                .build();
    }
    
    /**
     * <br>生成 _id 字段 
     *
     * @param projectType
     * @return
     * @author YellowTail
     * @since 2019-02-20
     */
    public static FieldSpec genId(ProjectType projectType) {
        if (ProjectType.C == projectType) {
            return FieldSpec.builder(String.class, "_id")
                  .addAnnotation(MongoObjectId.class)
                  .addModifiers(Modifier.PROTECTED)
                  .build();
        } else {
            return FieldSpec.builder(String.class, "_id")
                    .addAnnotation(Id.class)
                    .addModifiers(Modifier.PROTECTED)
                    .build();
        }
    }
    
    private static FieldSpec genNormalField(String fieldName, Class classType) {
        return FieldSpec.builder(classType, fieldName)
                .addModifiers(Modifier.PROTECTED)
                .build();
    }
    
    private static void genIdMethodIterable() {
        
    }
    
    private static void genGetSetIterable() {
        //Iterable
        MethodSpec getId = MethodSpec.methodBuilder("get_id")
                .returns(String.class)
                .addStatement("return $N", "_id")
                .build();
        
        MethodSpec setId = MethodSpec.methodBuilder("set_id")
                .addParameter(String.class, "_id")
                .addStatement("this.$N = $N", "_id", "_id")
                .build();
    }
    
    private static void genGetMethod() {
        
    }
}
