#### springboot启动流程：

1. springboot 生命周期：

   ~~~java
   // 核心接口：SpringApplicationRunListener，该接口定义了整个springboot的生命周期。
   // 它是SpringApplication类run方法的监听器
   // Listener for the {@link SpringApplication} {@code run} method.
   // 在不同的阶段，发布事件，调用具体的ApplicationListener类来进行处理。
   // 它只负责观察当前阶段，然后发布相应事件，具体其他的操作交由其他ApplicationListener监听器来实现自己的细节。
   public interface SpringApplicationRunListener {
   	// 在首次启动run方法时立即调用。可用于非常早的初始化。
   	default void starting() {
   	}
   	// 在环境准备好之后，但在创建{@link ApplicationContext}之前调用。
   	default void environmentPrepared(ConfigurableEnvironment environment) {
   	}
   	// 一旦创建并准备好{@link ApplicationContext}，但在加载源之前调用。
   	default void contextPrepared(ConfigurableApplicationContext context) {
   	}
   	// 一旦应用程序上下文已加载但在刷新之前调用。
   	default void contextLoaded(ConfigurableApplicationContext context) {
   	}
   	// 上下文已刷新，并且应用程序已启动，但是尚未调用{@link CommandLineRunner CommandLineRunners}和{@link ApplicationRunner ApplicationRunners}。
   	default void started(ConfigurableApplicationContext context) {
   	}
   	// 在刷新应用程序上下文并已调用所有{@link CommandLineRunner CommandLineRunners}和{@link ApplicationRunner ApplicationRunners}之前，在run方法完成之前立即调用。
   	default void running(ConfigurableApplicationContext context) {
   	}
   	// 运行应用程序时发生故障时调用。
   	default void failed(ConfigurableApplicationContext context, Throwable exception) {
   	}
   
   }
   
   ~~~

2. springboot启动流程：

   ~~~java
   // 核心启动方法
   public ConfigurableApplicationContext run(String... args) {
       // 基本准备工作
       StopWatch stopWatch = new StopWatch();
       stopWatch.start();
       ConfigurableApplicationContext context = null;
       Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
       configureHeadlessProperty();
       // 获取SpringApplicationRunListener监听器，默认的只有EventPublishingRunListener监听器
       SpringApplicationRunListeners listeners = getRunListeners(args);
       // 遍历所有的SpringApplicationRunListener监听器，调用starting方法。
       listeners.starting();
       try {
           ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
           // 在这个方法里，遍历所有的SpringApplicationRunListener监听器，调用environmentPrepared方法。
           ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
           configureIgnoreBeanInfo(environment);
           Banner printedBanner = printBanner(environment);
           context = createApplicationContext();
           exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
                                                            new Class[] { ConfigurableApplicationContext.class }, context);
           // 在这个方法里，遍历所有的SpringApplicationRunListener监听器，调用contextPrepared方法。
           prepareContext(context, environment, listeners, applicationArguments, printedBanner);
           refreshContext(context);
           afterRefresh(context, applicationArguments);
           stopWatch.stop();
           if (this.logStartupInfo) {
               new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
           }
           // 在这个方法里，遍历所有的SpringApplicationRunListener监听器，调用started方法。
           listeners.started(context);
           callRunners(context, applicationArguments);
       }
       catch (Throwable ex) {
           // 在这个方法里，遍历所有的SpringApplicationRunListener监听器，调用failed方法。
           handleRunFailure(context, ex, exceptionReporters, listeners);
           throw new IllegalStateException(ex);
       }
   
       try {
           // 在这个方法里，遍历所有的SpringApplicationRunListener监听器，调用running方法。
           listeners.running(context);
       }
       catch (Throwable ex) {
           handleRunFailure(context, ex, exceptionReporters, null);
           throw new IllegalStateException(ex);
       }
       return context;
   }
   ~~~

   