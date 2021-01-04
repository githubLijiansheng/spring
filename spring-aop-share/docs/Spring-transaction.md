# 聊聊 Spring 事物

虽然标题是 Spring 事物，但是 Spring 事物的基石是 Spring AOP，Spring AOP 的基础又是 JDK 的动态代理（CGLIB是另外一个话题），所以本次分享分为三大部分

1. 深入理解 JDK 动态代理
2. 深入理解 Spring AOP

   1. AOP 的基本概念
   2. Spring AOP 的配置方式 
   3. 从源码角度分析一下 Spring AOP 的实现方式（Spring AOP 依赖 Spring IOC）
   4. BeanFactory 和 FactoryBean 的区别（源码分析）
   
3. Spring 事物

先说一下本文不讲的内容（因为网上已有很多优秀的教程了）：

1. Spring 事物的传播机制
2. 事物的隔离级别
3. 事物的特性 ACID

本文讲的内容，一步一步，完整的走完 Spring 事物的执行流程。

### 1. 深入理解 JDK 动态代理

题目是深入理解 JDK 动态代理，而不是彻底理解，是因为有几个问题没搞明白。

1. 创建需要被代理的接口

```java
public interface UserService {
    String findOne(String name);
}

```

2. 创建实际被代理的对象

```java
public class UserServiceImpl implements UserService{
    @Override
    public String findOne(String name) {
        return "Hello " + name;
    }
}
```

3. 创建动态代理类

```java
public class DynamicProxy implements InvocationHandler {

    // 被代理的目标对象
    private Object target;
	// 使用构造方法将目标对象传入
    public DynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy :" + proxy.getClass());
        System.out.println("方法执行前，执行的动作");
        Object ret = method.invoke(target, args);
        System.out.println("方法返回结果:" + ret);
        System.out.println("方法执行后，执行的动作");
        return ret;
    }
}
```

4. 创建测试类

```java
public class Test {

    public static void main(String[] args) {
      	// 创建目标对象
        UserService userService = new UserServiceImpl();
        InvocationHandler invocation = new DynamicProxy(userService);
      	// 创建类加载器
        ClassLoader classLoader = userService.getClass().getClassLoader();
        Class<?>[] interfaces = userService.getClass().getInterfaces();
      	// 实例化代理对象
        UserService proxy = (UserService) Proxy.newProxyInstance(classLoader, interfaces, invocation);
        System.out.println("代理对象: " + proxy.getClass());
        proxy.findOne("张三");
    }
}
```

输出结果如下:

```java
代理对象: class com.sun.proxy.$Proxy0
proxy :class com.sun.proxy.$Proxy0
方法执行前，执行的动作
方法返回结果:Hello 张三
方法执行后，执行的动作
```

以上就是最基本JDK动态代理的实现，相信大家也都非常熟悉了。

有人可能觉得既然实例化了目标对象，这是动态代理吗？其实看了上面的代码，大家可能觉得动态代理很简单，但是心里总有一些疑问，但是还不知道问啥，这是最难受的，看似懂了吧，过一个月甚至两周再问动态代理，可能连上面简单的代码也写不出来了。

为了解决大家心中的疑惑，咱们再看一个没有目标对象版本：

```java
public class Test2 {

    public static void main(String[] args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        UserService proxy = (UserService) Proxy.newProxyInstance(classLoader, new Class[]{UserService.class}, 
        // 拉姆达表达式的方式实现 InvocationHandler
        (pro, method, param) -> "abc");
        System.out.println("代理对象: " + proxy.getClass());
        String str = proxy.findOne("张三");
        System.out.println("方法返回结果: " + str);
    }
}
```

输出结果如下：

```java
代理对象: class com.sun.proxy.$Proxy0
方法返回结果: abc
```

从上面的代码我们可以知道，没有目标对象的情况下，也可以实现动态代理，换句话说只有接口也可以实现方法调用，没有实现只是接口怎么能实现方法调用呢？这显然又是不合理的，但事实又是能调用的。其实这就是动态代理的强大之处，接口的实现类在运行的时候自动生成。

> 思考一个问题，这种没有目标对象的动态代理有什么应用场景？

我们知道了，接口的实现类是在代码运行中生成的，但是生成的实现类是什么样的，它是怎么执行的，还不大清楚？

5. 使用 JVM 参数打印出动态生成的代理对象

   在 main 方法运行时指定参数：`-Dsun.misc.ProxyGenerator.saveGeneratedFiles=true` 输出内存中的动态代理类到文件中。内容如下：

   ```java
public final class $Proxy0 extends Proxy implements UserService {
       private static Method m1;
       private static Method m3;
       private static Method m2;
       private static Method m0;
   
       public $Proxy0(InvocationHandler var1) throws  {
           super(var1);
       }
   
       public final boolean equals(Object var1) throws  {
           try {
               return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
           } catch (RuntimeException | Error var3) {
               throw var3;
           } catch (Throwable var4) {
               throw new UndeclaredThrowableException(var4);
           }
       }
   
       // 被代理的目标方法，即：userService.findOne("张三") 的时候被执行的代码 
       public final String findOne(String var1) throws  {
           try {
               /** 
                * super 是 Proxy
                * h 是 InvocationHandler
                * this 是动态代理类本身
                * m3 是被调用的方法
                * new Object[]{var1} 是方法调用需要传入的参数
                */
               return (String)super.h.invoke(this, m3, new Object[]{var1});
           } catch (RuntimeException | Error var3) {
               throw var3;
           } catch (Throwable var4) {
               throw new UndeclaredThrowableException(var4);
           }
       }
   
       public final String toString() throws  {
           try {
               return (String)super.h.invoke(this, m2, (Object[])null);
           } catch (RuntimeException | Error var2) {
               throw var2;
           } catch (Throwable var3) {
               throw new UndeclaredThrowableException(var3);
           }
       }
   
       public final int hashCode() throws  {
           try {
               return (Integer)super.h.invoke(this, m0, (Object[])null);
           } catch (RuntimeException | Error var2) {
               throw var2;
           } catch (Throwable var3) {
               throw new UndeclaredThrowableException(var3);
           }
       }
   
       // 静态代码块，在类加载的时候执行
       static {
           try {
               m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
               m3 = Class.forName("com.spring.share.aop.dynamicproxy.UserService").getMethod("findOne", Class.forName("java.lang.String"));
               m2 = Class.forName("java.lang.Object").getMethod("toString");
               m0 = Class.forName("java.lang.Object").getMethod("hashCode");
           } catch (NoSuchMethodException var2) {
               throw new NoSuchMethodError(var2.getMessage());
           } catch (ClassNotFoundException var3) {
               throw new NoClassDefFoundError(var3.getMessage());
           }
       }
   }
```
   
   到这是不是理解了动态代理呢，就像一般的方法调用那么简单。动态代理类是如何生成的？

### 2. 深入理解 Spring AOP

> 说明一点：这次说 Spring AOP是纯 Spring AOP 和 AspectJ 没有关系，但是 Spring 沿用了 Aspectj 的概念，包括也可能使用了 AspectJ 包里面的注解。例如：@Aspect、@Pointcut、@Before、@After 等

#### 1. 基本概念

Advice、Advisor、Pointcut、Joinpoint 等等，大家可能在学习时也接触过这些概念，但是都是当时明白，之后就忘了。所以这些概念就暂时忽略，用到啥再说对应的含义，当然 Spring 官方文档也有这些概念的定义，我没有复制下来。

#### 2. Spring AOP 的配置方式

1. 基于接口的配置，最原始的方式，早在 Spring 1.x 就有了
2. 基于 XML 的配置方式 `<aop/>`，在 Spring 2.0 时出现
3. 基于注解的方式，在 Spring 2.5 的时候引入

本文只讲第一种，这是最古老的，是最基础的，也是最容易理解的。下面使用示例的方式演示这种方式的使用。

#### 3. 基于接口的配置

首先定义一个接口`MessageService`以及对应的实现类`MessageServiceImpl`

```java
// 接口
public interface MessageService {

    void sendMessage(Message message);

    String sayHi(String name);
}

// 实现类
public class MessageServiceImpl implements MessageService {

    @Override
    public void sendMessage(Message message) {
        System.out.println(message);
    }

    @Override
    public String sayHi(String name) {
        System.out.println("接收到参数：" + name);
        return "Hello, " + name;
    }
}
```

再定义两个 advice（通知），这是我们接触的第一个概念，大家肯定都听说过前置通知，后置通知，环绕通知等，说的就是 `advice`，代码如下：

```java
// 前置通知
public class BeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("方法:" + method.getName() + "将要被执行，参数: " + args[0] + ", 目标对象: " + target.getClass());
    }
}

// 后置通知
public class AfterAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("方法:" + method.getName() + "被执行，参数: " + args[0] + ", 目标对象: " + target.getClass() + ", 返回结果: " + returnValue);
    }
}
```

根据名字也很明显，一个是在方法调用前执行，一个是在方法调用后执行。考虑一下环绕通知是怎么实现的？

编写 `Spring` 的配置文件

```xml
    <bean id="messageService" class="com.spring.share.aop.message.service.impl.MessageServiceImpl"/>
    <bean id="beforeAdvice" class="com.spring.share.aop.advice.BeforeAdvice"/>
    <bean id="afterAdvice" class="com.spring.share.aop.advice.AfterAdvice"/>

    <bean id="proxyFactoryBean" class="org.springframework.aop.framework.ProxyFactoryBean">
        <!-- 代理的接口 -->
        <property name="proxyInterfaces">
            <list>
                <value>com.spring.share.aop.message.service.MessageService</value>
            </list>
        </property>
        <!-- 目标对象 -->
        <property name="target" ref="messageService" />
        <!-- 这里的值可以是 advice, advisor, interceptor -->
        <property name="interceptorNames">
            <list>
                <value>beforeAdvice</value>
                <value>afterAdvice</value>
            </list>
        </property>
    </bean>
```

测试类代码如下：

```java
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
      	// 获取 Spring AOP 生成的代理类
        MessageService messageService = (MessageService) context.getBean("proxyFactoryBean");
        messageService.sendMessage(new Message(1122, "消息内容"));
        System.out.println("---------------");
        messageService.sayHi("张三");
    }
}
```

执行测试类代码，输出如下：

```java
方法:sendMessage将要被执行，参数: Message{id=1122, content='消息内容'}, 目标对象: class com.spring.share.aop.message.service.impl.MessageServiceImpl
Message{id=1122, content='消息内容'}
方法:sendMessage被执行，参数: Message{id=1122, content='消息内容'}, 目标对象: class com.spring.share.aop.message.service.impl.MessageServiceImpl, 返回结果: null
---------------
方法:sayHi将要被执行，参数: 张三, 目标对象: class com.spring.share.aop.message.service.impl.MessageServiceImpl
接收到参数：张三
方法:sayHi被执行，参数: 张三, 目标对象: class com.spring.share.aop.message.service.impl.MessageServiceImpl, 返回结果: Hello, 张三
```

从结果来看非常好理解，`messageService` 的两个方法执行前后均被拦截了，这和我们所期望的也正好一致，这就是最原始的 Spring AOP 的实现方式，但是它不够灵活，也不够方便，再加上咱们接触到的最早的也都是 `xml`的配置，到现在 `xml` 几乎也没有了，全是注解了，所以大家都不大清楚。

但是这有一个问题，`messageService` 接口里面的所有方法都被拦截了，这可能不是我们需要的，例如事物，我们可能只需要拦截增删改的方法，而查询的方法不需要拦截。

下面介绍下一个概念介  `advisor`，中文含义 顾问，它可以实现拦截指定的方法，配置也比较简单，内部只需要指定一个 `advice`。`advisor` 负责决定拦截哪些方法，具体的拦截工作还是由 `advice` 完成。

`advisor` 实现类有很多，选择一个简单的演示一下，`NameMatchMethodPointcutAdvisor`，见名知意，根据方法名称进行匹配。配置如下：

```xml
    <bean id="messageService" class="com.spring.share.aop.message.service.impl.MessageServiceImpl"/>
    <bean id="beforeAdvice" class="com.spring.share.aop.advice.BeforeAdvice"/>
    <bean id="afterAdvice" class="com.spring.share.aop.advice.AfterAdvice"/>

    <bean id="springAdvisor" class="org.springframework.aop.support.NameMatchMethodPointcutAdvisor">
        <property name="advice" ref="beforeAdvice" />
      	<!-- 只拦截了 sayHi 方法，可指定多个，使用 逗号 分隔 --> 
        <property name="mappedNames" value="sayHi" />
    </bean>
    <bean id="proxyFactoryBean" class="org.springframework.aop.framework.ProxyFactoryBean">
        <property name="proxyInterfaces">
            <list>
                <value>com.spring.share.aop.message.service.MessageService</value>
            </list>
        </property>
        <property name="target" ref="messageService" />
        <property name="interceptorNames">
            <list>
                <value>springAdvisor</value>
            </list>
        </property>
    </bean>
```

运行效果如下：

```
方法:sayHi将要被执行，参数: 张三, 目标对象: class com.spring.share.aop.message.service.impl.MessageServiceImpl
接收到参数：张三
```

运用的效果和我们预计的也一致。再次验证了 `advisor` 负责拦截哪些方法，而具体的拦截工作由 `advice` 来完成。



前面说过，`interceptorNames` 除了是 `advice` 和 `advisor` 之外，还可以是 `interceptor`，对于Java 开发者来说，`Interceptor` 再熟悉不过了，演示一个 java doc 的实例，具体参考 java doc 文档 `org.aopalliance.intercept.Interceptor`。

```java
// 这是 Interceptor java doc 文档的一部分
class DebuggingInterceptor implements MethodInterceptor, 
    ConstructorInterceptor, FieldInterceptor {
   Object invoke(MethodInvocation i) throws Throwable {
     debug(i.getMethod(), i.getThis(), i.getArgs());
     return i.proceed();
   }

   Object construct(ConstructorInvocation i) throws Throwable {
     debug(i.getConstructor(), i.getThis(), i.getArgs());
     return i.proceed();
   }
 
   Object get(FieldAccess fa) throws Throwable {
     debug(fa.getField(), fa.getThis(), null);
     return fa.proceed();
   }

   Object set(FieldAccess fa) throws Throwable {
     debug(fa.getField(), fa.getThis(), fa.getValueToSet());
     return fa.proceed();
   }

   void debug(AccessibleObject ao, Object this, Object value) {
     ...
   }
}
```

从上面的代码可知，拦截的对象可以是构造方法，属性，方法等，在日常开发中，比较常用的还是对方法的拦截，针对其他的拦截暂不考虑。

编写一个拦截器

```java
public class LoggerInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Before: invocation=");
        // 执行 真实实现类 的方法
        Object ret = invocation.proceed();
        System.out.println("Invocation returned");
        return ret;
    }
}
```

`application.xml`配置如下：

```java
    <bean id="messageService" class="com.spring.share.aop.message.service.impl.MessageServiceImpl"/>
      
	<bean id="loggerInterceptor" class="com.spring.share.aop.interceptor.LoggerInterceptor"></bean>
    <bean id="proxyFactoryBean" class="org.springframework.aop.framework.ProxyFactoryBean">
        <property name="proxyInterfaces">
            <list>
                <value>com.spring.share.aop.message.service.MessageService</value>
            </list>
        </property>
        <property name="target" ref="messageService" />
        <property name="interceptorNames">
            <list>
                <value>loggerInterceptor</value>
            </list>
        </property>
    </bean>
```

输出结果如下：

```
Before: invocation
Message{id=1122, content='消息内容'}
Invocation returned
---------------
Before: invocation
接收到参数：张三
Invocation returned
```

由结果可知，`interceptor` 拦截了所有方法，并且在方法前后都进行了拦截，由此可知环绕通知可以通过`interceptor` 实现，`interceptor` 结合 `advisor` 可以实现针对某些方法实现环绕通知。

综上，根据以上示例，大家应该都明白 **advice**，**advisor**，**interceptor** 的概念了吧。

下面再看一个问题，针对每个 Service 接口都得配置一个代理类 `org.springframework.aop.framework.ProxyFactoryBean`，现实项目中肯定有多个 Service 接口，这样显然很不方便，为此`Spring` 实现了自动代理的方式 **autoproxy**，此处强调**自动**。

如何实现自动，可参考 `RegexpMethodPointcutAdvisor` 和 `BeanNameAutoProxyCreator` 实现自动化，具体内容暂不展开讲了。

#### 5. 从源码角度分析一下 Spring AOP 的实现方式

先看一下 `application.xml` 的配置

```xml
<bean id="proxyFactoryBean" class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="proxyInterfaces">
        <list>
            <value>com.spring.share.aop.message.service.MessageService</value>
        </list>
    </property>
    <property name="target" ref="messageService" />
    <property name="interceptorNames">
        <list>
            <value>loggerInterceptor</value>
        </list>
    </property>
</bean>
```

测试类的代码如下：

```java
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        // 获取 Spring AOP 生成的代理类
        MessageService messageService = (MessageService) context.getBean("proxyFactoryBean");
        messageService.sendMessage(new Message(1122, "消息内容"));
        System.out.println("---------------");
        messageService.sayHi("张三");
    }
}
```

我们在 `context.getBean("proxyFactoryBean")` 返回的对象却可以向上类型转换为 `MessageService`，不应该是 `ProxyFactoryBean` 吗？

下面从源码的角度一步一步分析一下为什么返回的不是 `ProxyFactoryBean`，以及代理类在 Spring 中是如何创建的（一步一步代码debug）

### 3. Spring 事物

说到事物，大家都很清楚，大体分三个部分：

* 一，开启事物（在 Spring 中是获取事物对象）
* 二，执行SQL语句，完成之后进行提交
* 三、执行SQL语句，异常进行数据混滚

正如 Spring 事物管理器顶层接口定义的那样，就只有三个方法，具体见 `org.springframework.transaction.PlatformTransactionManager`

在前面学习了 Spring AOP 之后，大家肯定也猜到了，在执行某个标记了需要事物的 Service 接口时，肯定会执行其代理类，代理类在执行时，肯定是先获取事物对象，再使用反射调用目标方法，最后如果正常则执行提交操作，如果发生异常，则在 catch 代码块中进行回滚。

**事实也是如此**。具体见 `org.springframework.transaction.interceptor.TransactionAspectSupport`

但是，具体是怎么执行的呢，代码是怎么一步一步串联起来的，好像还不大清楚。具体见演示示例（一步一步代码debug）

##### 1. 关于 Spring 事物的配置说明：

关于事务配置总是由三个组成部分，分别是DataSource、TransactionManager和代理机制这三部分，无论哪种配置方式，一般变化的只是代理配置这部分。所以，无论是网上还是实际工作中，大家肯定见过各种各样的配置，光 xml 的就有好几种，还有基于注解的配置，总结一下的话，没有十多种也有五六种，但是追踪到 Spring 底层的源码发现，只有一种实现，万变不离其宗，所有的变化都仅仅是一种表象而已。

### 4. 总结

