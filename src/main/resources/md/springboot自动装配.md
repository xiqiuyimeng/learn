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

  - @Import(AutoConfigurationImportSelector.class)

    - @Import注解，springboot自动装配的核心注解

      - 参数如果是普通类，将该类实例化交给IOC容器管理。
      - 参数如果是ImportBeanDefinitionRegistrar的实现类，支持手工注册Bean。
      - 参数如果是ImportSelector的实现类，注册selectImports返回的数组（类的全路径）到IOC容器，实现批量注册。

    - 核心类：AutoConfigurationImportSelector实现DeferredImportSelector（DeferredImportSelector实现ImportSelector接口）接口，重点是selectImports方法：

      ~~~java
      	@Override
      	public String[] selectImports(AnnotationMetadata annotationMetadata) {
      		if (!isEnabled(annotationMetadata)) {
      			return NO_IMPORTS;
      		}
      		AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader
      				.loadMetadata(this.beanClassLoader);
              // 重要方法：获取自动配置项
      		AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(autoConfigurationMetadata,
      				annotationMetadata);
      		return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
      	}
      
      
      	protected AutoConfigurationEntry getAutoConfigurationEntry(AutoConfigurationMetadata autoConfigurationMetadata,
      			AnnotationMetadata annotationMetadata) {
      		if (!isEnabled(annotationMetadata)) {
      			return EMPTY_ENTRY;
      		}
      		AnnotationAttributes attributes = getAttributes(annotationMetadata);
              // 装配的核心：获取候选配置项
      		List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
      		configurations = removeDuplicates(configurations);
      		Set<String> exclusions = getExclusions(annotationMetadata, attributes);
      		checkExcludedClasses(configurations, exclusions);
      		configurations.removeAll(exclusions);
      		configurations = filter(configurations, autoConfigurationMetadata);
      		fireAutoConfigurationImportEvents(configurations, exclusions);
      		return new AutoConfigurationEntry(configurations, exclusions);
      	}
      
      
      	protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
              // 真正进行装配的类：SpringFactoriesLoader，
      		List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
      				getBeanClassLoader());
      		Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
      				+ "are using a custom packaging, make sure that file is correct.");
      		return configurations;
      	}
      
      
      
      ~~~

      核心装配类：SpringFactoriesLoader

      ~~~java
      
      	public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
      		String factoryTypeName = factoryType.getName();
      		return loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
      	}
      
      	/**
      	 * The location to look for factories.
      	 * <p>Can be present in multiple JAR files.
      	 * 自动装配的配置文件地址，地址为相对地址，注释解释可以在多个jar包中存在，也就是自定义开发需要自动装配的starter的时候，可以使用这个配置来进行自动装配
      	 */
      	public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
      
      	private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
      		MultiValueMap<String, String> result = cache.get(classLoader);
      		if (result != null) {
      			return result;
      		}
      
      		try {
      		// 这里定义了自动装配的资源文件地址
      			Enumeration<URL> urls = (classLoader != null ?
      					classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
      					ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
      			result = new LinkedMultiValueMap<>();
      			while (urls.hasMoreElements()) {
      				URL url = urls.nextElement();
      				UrlResource resource = new UrlResource(url);
      				Properties properties = PropertiesLoaderUtils.loadProperties(resource);
      				for (Map.Entry<?, ?> entry : properties.entrySet()) {
      					String factoryTypeName = ((String) entry.getKey()).trim();
      					for (String factoryImplementationName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
      						result.add(factoryTypeName, factoryImplementationName.trim());
      					}
      				}
      			}
      			cache.put(classLoader, result);
      			return result;
      		}
      		catch (IOException ex) {
      			throw new IllegalArgumentException("Unable to load factories from location [" +
      					FACTORIES_RESOURCE_LOCATION + "]", ex);
      		}
      	}
      ~~~

    - META-INF/spring.factories文件：

      ~~~java
      // 自动配置的格式：org.springframework.boot.autoconfigure.EnableAutoConfiguration=配置类，将需要装配的配置类写在这里，就会自动装配
      # Auto Configure
      org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
      org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
      org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
      org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
      org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
      org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
      org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration,\
      org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration,\
      org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration,\
      org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration,\
      org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration,\
      org.springframework.boot.autoconfigure.webservices.client.WebServiceTemplateAutoConfiguration
      
      ~~~
      
    - @Import注解的实现，核心类：ConfigurationClassParser

      ~~~java
      // 在springboot启动过程中，首先准备环境、创建并准备上下文、发布上下文已加载信号后，执行刷新上下文的操作，在刷新的过程中进行解析@Import注解。
      // 刷新上下文，在调用beanFactory的后置处理器时解析注解，具体的后置处理器为 ConfigurationClassPostProcessor
      @Override
      public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
      	// 省略部分代码 
          processConfigBeanDefinitions(registry);
      }
      // 调用此方法进行处理
      public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
          List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
          String[] candidateNames = registry.getBeanDefinitionNames();
      
      	// 实例化解析器，进行解析操作
          // Parse each @Configuration class
          ConfigurationClassParser parser = new ConfigurationClassParser(
              this.metadataReaderFactory, this.problemReporter, this.environment,
              this.resourceLoader, this.componentScanBeanNameGenerator, registry);
      
          Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);
          Set<ConfigurationClass> alreadyParsed = new HashSet<>(configCandidates.size());
          do {
              // 调用解析器的解析方法
              parser.parse(candidates);
              parser.validate();
      
             // 省略部分代码
          }
          while (!candidates.isEmpty());
      
          // Register the ImportRegistry as a bean in order to support ImportAware @Configuration classes
          if (sbr != null && !sbr.containsSingleton(IMPORT_REGISTRY_BEAN_NAME)) {
              sbr.registerSingleton(IMPORT_REGISTRY_BEAN_NAME, parser.getImportRegistry());
          }
      
          if (this.metadataReaderFactory instanceof CachingMetadataReaderFactory) {
              // Clear cache in externally provided MetadataReaderFactory; this is a no-op
              // for a shared cache since it'll be cleared by the ApplicationContext.
              ((CachingMetadataReaderFactory) this.metadataReaderFactory).clearCache();
          }
      }
      
      // ConfigurationClassParser 类
      // 这里对类定义的处理，只是将其分门别类的放置好，并未完成bean的任何操作
      public void parse(Set<BeanDefinitionHolder> configCandidates) {
          for (BeanDefinitionHolder holder : configCandidates) {
              BeanDefinition bd = holder.getBeanDefinition();
              try {
                  // 判断是注解bean的时候，调用解析方法进行解析
                  if (bd instanceof AnnotatedBeanDefinition) {
                      parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
                  }
                  else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass()) {
                      parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
                  }
                  else {
                      parse(bd.getBeanClassName(), holder.getBeanName());
                  }
              }
              catch (BeanDefinitionStoreException ex) {
                  throw ex;
              }
              catch (Throwable ex) {
                  throw new BeanDefinitionStoreException(
                      "Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);
              }
          }
      
          this.deferredImportSelectorHandler.process();
      }
      
      protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException {
          processConfigurationClass(new ConfigurationClass(metadata, beanName), DEFAULT_EXCLUSION_FILTER);
      }
      
      protected void processConfigurationClass(ConfigurationClass configClass, Predicate<String> filter) throws IOException {
      	// 省略部分代码
          // Recursively process the configuration class and its superclass hierarchy.
          SourceClass sourceClass = asSourceClass(configClass, filter);
          do {
              // 调用真正的处理注解的方法
              sourceClass = doProcessConfigurationClass(configClass, sourceClass, filter);
          }
          while (sourceClass != null);
      
          this.configurationClasses.put(configClass, configClass);
      }
      
      // 核心处理注解方法
      protected final SourceClass doProcessConfigurationClass(
      			ConfigurationClass configClass, SourceClass sourceClass, Predicate<String> filter)
      			throws IOException {
      
          // Process any @PropertySource annotations
      	// 省略代码
      
          // Process any @ComponentScan annotations
      	// 省略代码
      
          // 这里是处理@Import注解的地方
          // Process any @Import annotations
          processImports(configClass, sourceClass, getImports(sourceClass), filter, true);
      
          // Process any @ImportResource annotations
      	// 省略代码
      
          // Process individual @Bean methods
       	// 省略代码
      
          // Process default methods on interfaces
          processInterfaces(configClass, sourceClass);
      
          // Process superclass, if any
       	// 省略代码
      
          // No superclass -> processing is complete
          return null;
      }
      
      // @Import注解的处理方法
      private void processImports(ConfigurationClass configClass, SourceClass currentSourceClass,
      			Collection<SourceClass> importCandidates, Predicate<String> exclusionFilter,
      			boolean checkForCircularImports) {
      
          if (checkForCircularImports && isChainedImportOnStack(configClass)) {
              this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
          }
          else {
              this.importStack.push(configClass);
              try {
                  for (SourceClass candidate : importCandidates) {
                      // 首先判断是否是ImportSelector实现类
                      // 候选类是一个ImportSelector->委托给它以确定导入
                      if (candidate.isAssignable(ImportSelector.class)) {
                          // 省略代码
                      }
                      // 判断是否是ImportBeanDefinitionRegistrar实现类
                      // 候选类是一个ImportBeanDefinitionRegistrar->委托给它注册其他bean定义
                      else if (candidate.isAssignable(ImportBeanDefinitionRegistrar.class)) {
                          // 省略代码
                      }
                      // 候选类不是ImportSelector或ImportBeanDefinitionRegistrar->将其作为@Configuration类处理，如果是普通类，暂时不处理，只存储到configurationClasses  map中。
                      else {
       					// 省略代码
                      }
                  }
              }
              catch (BeanDefinitionStoreException ex) {
                  throw ex;
              }
              catch (Throwable ex) {
                  throw new BeanDefinitionStoreException(
                      "Failed to process import candidates for configuration class [" +
                      configClass.getMetadata().getClassName() + "]", ex);
              }
              finally {
                  this.importStack.pop();
              }
          }
      }
      
      ~~~

  - 总结：

    - @SpringBootApplication 注解标明该类是springboot启动类。
      - @EnableAutoConfiguration 注解实现自动装配
        - @Import(AutoConfigurationImportSelector.class)注解，注册AutoConfigurationImportSelector类中selectImports方法获取到的需要装配的Bean（通过读取 META-INF/spring.factories 配置文件来获取需要装配的Bean）。

- 自定义starter的开发：

  - 新建两个模块，模块的命名规范为：xxx-spring-boot-starter（也可以合成一个模块）
    - xxx-spring-boot-starter 模块负责管理starter中的pom依赖，外部引用只需要引用此模块，这个模块会依赖于autoconfigure模块（可以认为是个空壳）。
    - xxx-spring-boot-autoconfigure 模块负责完成自动配置的核心功能，包括业务功能实现。
    - 对于springboot自带的starter，命名为：spring-boot-starter-xxx
  - 使用@ConfigurationProperties注解接收配置文件中的参数，相当于批量的@Value注解。
  - 使用@Configuration + @Bean来注册需要的Bean，使用@EnableConfigurationProperties注解开启参数接收，主要是配合@ConfigurationProperties注解。
  - 加载配置类：
    - 使用 META-INF/spring.factories 配置文件来加载配置类。这种形式比较好，轻依赖，写在autoconfigure模块中。
    - 在启动类上使用@Import注解加载配置类。
    - 自定义注解加载配置类。
    
    
