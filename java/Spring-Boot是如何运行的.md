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



被注解的注解就是上面说的组合注解。Spring Framework本身实现了很多组合注解，比如**@Configuration**就是一个组合注解。因此有了这样的一个条件，Spring-Boot的实现才有了基础条件。

## 条件注解



## 运行原理

