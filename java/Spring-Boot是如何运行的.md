# Spring-Boot是如何运行的 #

![](https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2656353677,2997395625&fm=26&gp=0.jpg)

初识Spring-Boot时，认为其使用方式与Spring Framework并无多大差异，无非就是将配置注解化，提供更多的基础能力，使开发人员更加简单快速的开发自身业务的升级版Spring Framework。然而在使用了一段时间后发现其实并没有这么简单，Spring再一次的惊艳到我，这种设计理念简直优秀。为此对Spring-Boot的实现原理有了一定兴趣。在网上看了一些大牛的文章，将其转载过来，用以学习。



# Spring-Boot运行原理

实际上Spring-Boot并不是要取代Spring Framework。在JDK1.5+之后Spring Framework实现了大量的注解来代替原有的XML配置。主要用于配置管理、Bean注入以及AOP等相关功能实现。然而随着Spring注解越来越多，并且被大量使用，导致了大量繁琐的配置以及冗余代码。

其实在实际运用中XML并非不是最优选择，个人认为在核心的基础配置上使用XML来配置，将基础能力与实际业务分离可以增加整个项目代码可读性，反而基础能力配置的注解过多会导致代码可读性较差，也增加了团队能力的深度。所以孰是孰非并无绝对定义。还是需要根据自身团队、项目实际情况来选择。

那么Spring难道会意识不到这点吗？

大量的注解能否组合一下？通过定义一些新的注解，将功能进行分类，不同的注解通过新的注解进行一定的组合，这样对于大部分应用场景下，只需要使用一个新的注解就自动包含了一些其他功能的注解。**Spring-Boot也就是基于这个概念出发而来的产物**。

但是，要**实现注解的组合并不是简单的把多个注解牵强的叠加在一起**，这里涉及到一些编程语言上的实现，例如要组合一个注解，那么该注解是否支持注解到别的注解上呢（略微有点拗口）？还有如果组合注解后，因为注解的背后还涉及到**Spring容器上下文的初始化**以及Bean注入相关的逻辑，如果一个A注解涉及的Bean，涉及到另外一个B注解涉及到的Bean的初始化；也就意味着A注解的Bean初始化，需要在B注解的Bean初始化完成后才能进行注入，否则就会导致Bean依赖注入的失败。



**Spring Boot框架本质上就是通过组合注解的方式实现了诸多Spring注解的组合**，从而极大地简化了Spring框架本身的繁琐配置，实现快速的集成和开发。只是要这样实现，也需要一定的**基础条件**！



## 元注解

其实Spring-Boot是在Spring Framework的基础之上做的一次二次封装，最重要的特点就是Spring-Boot定义了一些新的注解来实行一些Spring注解 组合。而Spring注解则是基于JDK1.5+后的注解功能支持来完成的。

首先要了解元注解：

- 定义

```java
@Retention //定义注解的保留策略

@Retention(RetentionPolicy.SOURCE) //注解仅存在于源码中 ， 在class字节码文件中不包含

@Retention(RetentionPolicy.CLASS) //默认的保留策略，在class字节码文件中存在，在运行时无法获得

@Retention(RetentionPolicy.RUNTIME) //在class字节码文件中存在，运行时可通过反射获得
```

需要知道注解的生命周期 SOURCE < CLASS < RUNTIME ，所以前者作用的地方后者一定作用。

一般需要在运行时去动态的获得注解，则只能使用RUNTIME；

需要在编译时进行一些预处理，比如生成一些辅助代码，就用CLASS注解；

只是做一些检查性操作，比如@Override，使用SOURCE注解。



- 目标

  ```java
  @Target //定义注解作用目标
  
  @Target(ElementType.TYPE)   //接口、类、枚举、注解
  
  @Target(ElementType.FIELD) //字段、枚举的常量
  
  @Target(ElementType.METHOD) //方法
  
  @Target(ElementType.PARAMETER) //方法参数
  
  @Target(ElementType.CONSTRUCTOR)  //构造函数
  
  @Target(ElementType.LOCAL_VARIABLE)//局部变量
  
  @Target(ElementType.ANNOTATION_TYPE)//注解
  
  @Target(ElementType.PACKAGE) ///包
  
   
  @Document //说明该注解将被包含在javadoc中
  @Inherited //说明子类可以继承父类中的该注解
  ```

  举例

  ```java
  // 适用类、接口（包括注解类型）或枚举  
  @Retention(RetentionPolicy.RUNTIME)  
  @Target(ElementType.TYPE)  
  public @interface ClassInfo {  
      String value();  
  }  
      
  // 适用field属性，也包括enum常量  
  @Retention(RetentionPolicy.RUNTIME)  
  @Target(ElementType.FIELD)  
  public @interface FieldInfo {  
      int[] value();  
  }  
  
  // 适用方法  
  @Retention(RetentionPolicy.RUNTIME)  
  @Target(ElementType.METHOD)  
  public @interface MethodInfo {  
      String name() default "long";  
      String data();  
      int age() default 27;  
  }  
  ```



被注解的注解就是上面说的组合注解。Spring Framework本身实现了很多组合注解，比如 **@Configuration** 就是一个组合注解。因此有了这样的一个条件，Spring-Boot的实现才有了基础条件。



## 条件注解 @Conditional



Spring 4提供了一个通用的基于条件的注解 **@Conditional** ，该注解可以**根据满足某一个特定条件与否来决定是否创建某个Bean** ， 例如，某个依赖包jar在一个类路径的时候，自动配置一个或多个Bean时，可以通过**@Conditional** 注解来实现只有某个Bean被创建时才会创建另一个Bean。这样可以依据特定条件来控制Bean的创建行为。这样的话就可以通过这个特性机制来实现一些**自动配置**。



而这一点对于Spring-Boot实现自动配置来说是一个核心基础能力。在Spring-Boot中以**@Conditional** 为元注解用重新定义了一组针对不同场景的组合条件注解：

```java
@ConditionalOnBean //当容器中有指定Bean的条件下进行实例化

@ConditionalOnMissingBean //当容器中没有指定Bean的条件下进行实例化

@ConditionalOnClass //当classpath类路径下有指定类的条件下进行实例化

@ConditionalOnMissingClass //当classpath类路径下没有指定类的条件下进行实例化

@ConditionalOnWebApplication //当project是一个web时进行实例化

@ConditionalOnNotWebApplication //当project不是一个web时进行实例化

@ConditionalOnProperty //当指定的属性有指定的值时进行实例化

@ConditionalOnExpression //基于SpEL表达式条件判断

@ConditionalOnJava //当JVM版本为指定的版本范围时触发实例化

@ConditionalOnResource //当类路径下有指定的资源时触发实例化

@ConditionalOnJndi //在Jndi的条件下触发实例化

@ConditionalOnSingleCandidate //当指定Bean在容器中有一个，或者多个但是指定了首选Bean时触发实例化
```



纵观Spring-Boot的一些核心注解，基于@Conditional元注解的组合注解就占了很大部分，在Spring-Boot源码项目**spring-boot-autoconfigure** 中，随意打开一个AutoConfiguration文件，都会看到上述条件注解的使用。

```java
@Configuration
@ConditionalOnClass(DSLContext.class)
@ConditionalOnBean(DataSource.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class,
      				 TransactionAutoConfiguration.class })
public class JooqAutoConfiguration {

   @Bean
   @ConditionalOnMissingBean
   public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource ds) {
      return new DataSourceConnectionProvider(
            new TransactionAwareDataSourceProxy(ds)
      );
   }

   @Bean
   @ConditionalOnBean(PlatformTransactionManager.class)
   public SpringTransactionProvider transactionProvider(PlatformTransactionManager m) {
      return new SpringTransactionProvider(m);
   }
    
    //...
}
```





## 运行原理

前面了解了Spring-Boot自动配置的一些关键点，现在结合Spring-Boot最核心的组合注解**@SpringBootApplication** 来分析Spring-Boot是如何启动运行的。

![](https://github.com/wjse/blog/blob/master/java/assets/Spring-Boot是如何运行的.png)

@SpringBootApplication除了对应用开放的@ComponentScan注解（实现对开发者定义的应用包扫描）外，其最核心的注解就是**@EnableAutoConfiguration** ,该注解表示开启自动配置功能，而在具体的实现上则是通过导入**@Import（EnableAutoConfigurationImportSelector.class）** 类的实例。在逻辑上实现了对所依赖的核心jar下**META/INFO/spring-factories**文件的扫描。该文件申明了有哪些自动配置需要被Spring容器加载，从而Spring-Boot应用程序就能自动加载Spring核心容器配置，以及其他依赖的项目组件配置，从而最终完成应用的自动初始化，通过这种方法就像开发者屏蔽了启动加载的过程。



如“spring-boot-autoconfigure”核心包中的**META-INF/spring.factories**文件就是定义了需要加载的Spring Boot项目所依赖的基础配置类，如Spring的容器初始化配置类等。如：



```java
# Initializers
org.springframework.context.ApplicationContextInitializer=\
org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer,\
org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener

# Application Listeners
org.springframework.context.ApplicationListener=\
org.springframework.boot.autoconfigure.BackgroundPreinitializer

# Auto Configuration Import Listeners
org.springframework.boot.autoconfigure.AutoConfigurationImportListener=\
org.springframework.boot.autoconfigure.condition.ConditionEvaluationReportAutoConfigurationImportListener

# Auto Configuration Import Filters
org.springframework.boot.autoconfigure.AutoConfigurationImportFilter=\
org.springframework.boot.autoconfigure.condition.OnClassCondition

# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\

......
```

而对于大部分第三方需要与Spring-Boot集成的框架，或者自己日常开发中需要进行抽象的公共组件而言，得益于这种机制，也可以很容易地定制成开箱即用的各种starter组件。而使用这些组件的用户，往往只需要将依赖引入就好，不再需要进行任何额外的配置了！