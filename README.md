# Ananotion-Processor
Ananotion-Processor with Maven And Gradle demo  
提供了一个 demo 工程，来演示 如何在`Maven` `Gradle`里配置、执行 注解处理器 (输出日志即可认为调用成功)  

## Projects 工程介绍

|name|descrption|
|-|-|
|`yellow-proceesor`| 包含`注解`和`注解处理器`的工程，以`jar`包形式对外提供服务|
|`yellow-doc`| 使用了注解，并且希望在编译的时候，执行注解处理器进行一些操作的工程|

## PreRun 运行前准备
需要先`install` 注解处理器所在的工程，方便 `yellow-doc` 调用

```
cd yellow-proceesor
mvn install
```

`install`完成之后，需要到 `yellow-doc` 工程里修改一下本地 maven repo 路径  
当然，传到你自己的私服也是可以的

## Run 运行
可以到 `yellow-doc` 里执行命令，来查看是否输出了日志，进而确定 注解处理器 是否配置正确

### Maven
```
cd yellow-doc
mvn clean package
```

输出为
```
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------------< com.yellow:yellow-doc >------------------------
[INFO] Building yellow-doc 1.0
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ yellow-doc ---
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ yellow-doc ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory D:\006-project\yellowtail\Annotion-Processor\yellow-doc\src\main\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:compile (default-compile) @ yellow-doc ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 1 source file to D:\006-project\yellowtail\Annotion-Processor\yellow-doc\target\classes
bbbbbbbbbbb
输入的所有类有：
>>> Document
需要生成相应接口的类有：
>>> name
>>> FIELD
is field
>>> age
>>> FIELD
is field
-------------------注解处理器第1次循环处理结束...

输入的所有类有：
需要生成相应接口的类有：
-------------------注解处理器第2次循环处理结束...

[INFO]
...
```

### Gradle

```
cd yellow-doc
gradle assemble
```

(如果本地没有配置 gradle，那么就执行 `./gradle.bat assemble``)

输出为
```
> Task :compileJava
bbbbbbbbbbb
输入的所有类有：
>>> Document
需要生成相应接口的类有：
>>> name
>>> FIELD
is field
>>> age
>>> FIELD
is field
-------------------注解处理器第1次循环处理结束...

注: process() is execute...
输入的所有类有：
需要生成相应接口的类有：
-------------------注解处理器第2次循环处理结束...

注: process() is execute...

BUILD SUCCESSFUL in 0s
```