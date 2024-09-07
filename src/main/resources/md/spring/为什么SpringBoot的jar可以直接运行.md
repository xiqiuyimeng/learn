##### 为什么SpringBoot的jar可以直接运行

1. SpringBoot提供了一个插件，spring-boot-maven-plugin，用于将程序打包成一个可执行的jar包
2. SpringBoot应用打包之后，会生成一个Fat jar（jar里面包含很多依赖的jar），包含所有依赖的jar包和 Spring Boot Loader相关的类
3. java -jar命令，会去找jar中的 manifest文件，文件中会描述真正的启动类，Main-class
4. Fat jar的启动Main函数是JarLauncher，它负责创建一个LaunchedURLClassLoader，来加载boot-lib下面的jar，并以一个新线程启动应用的Main函数