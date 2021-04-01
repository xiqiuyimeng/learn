### springboot自动装配及自定义starter开发

- springboot 的启动包：spring-boot-starter

- springboot可以省略版本号的原因：

  ```java
      <parent>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>2.2.7.RELEASE</version>
          <relativePath/> <!-- lookup parent from repository -->
      </parent>
          
      //定义了核心依赖包的版本号
    	<parent>
      	<groupId>org.springframework.boot</groupId>
      	<artifactId>spring-boot-dependencies</artifactId>
      	<version>2.2.7.RELEASE</version>
      	<relativePath>../../spring-boot-dependencies</relativePath>
    	</parent>
          
      //配置文件是在spring-boot-starter-parent的pom文件中指定加载
        <resource>
          <filtering>true</filtering>
          <directory>${basedir}/src/main/resources</directory>
          <includes>
            <include>**/application*.yml</include>
            <include>**/application*.yaml</include>
            <include>**/application*.properties</include>
          </includes>
        </resource>
  ```

- 自动装配原理

  ~~~java
  // SpringBootApplication注解：
      
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @Inherited
  @SpringBootConfiguration
  @EnableAutoConfiguration
  @ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
  		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
  public @interface SpringBootApplication {
     
  // 这四个是Java元注解，用来修饰自定义注解
  @Target(ElementType.TYPE)	// 定义注解的作用范围 类、方法、属性
  @Retention(RetentionPolicy.RUNTIME)	// 定义注解的生命周期（编译器、运行期）
  @Documented	// Javadoc
  @Inherited	// 修饰的自定义注解可被子类继承
  ~~~

  - @SpringBootConfiguration注解

    ~~~java
    // @SpringBootConfiguration 相当于 @Configuration注解
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Configuration	// 保证Bean的唯一性
    public @interface SpringBootConfiguration {
        
    	@AliasFor(annotation = Configuration.class)
    	boolean proxyBeanMethods() default true;	//默认使用CGLIB代理该类
    
    }
    ~~~

  - @EnableAutoConfiguration注解

    ~~~java
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @AutoConfigurationPackage
    @Import(AutoConfigurationImportSelector.class)	//导入参数到IOC容器
    public @interface EnableAutoConfiguration {
    
    	String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";
    
    	Class<?>[] exclude() default {};
    
    	String[] excludeName() default {};
    
    }
    
    ~~~

  - @Import注解，springboot自动装配的核心注解

    - 参数如果是普通类，将该类实例化交给IOC容器管理。
    - 参数如果是ImportBeanDefinitionRegistrar的实现类，支持手工注册Bean。
    - 参数如果是ImportSelector的实现类，注册selectImports返回的数组（类的全路径）到IOC容器，实现批量注册。

  - @AutoConfigurationPackage注解

    ~~~java
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @Import(AutoConfigurationPackages.Registrar.class)	//保存扫描路径，提供给spring-data-jpa查询使用
    public @interface AutoConfigurationPackage {
    
    }
    ~~~

    

 