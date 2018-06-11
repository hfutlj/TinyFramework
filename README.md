 ## TinyFramework是一款简易的Web框架，包含了最基础的javaWeb框架的功能，AOP、IOC、MVC...仅供了解和学习Web框架的原理使用，会逐步添加ORM等功能，不断完善。

 ### 使用
 1. 引入：
 ```xml
 <dependency>
 	<groupId>TinyFramework</groupId>
	     <artifactId>TinyFramework</artifactId>
	     <version>1.0.0</version>
</dependency>
 ```
 2.  使用示例：  
使用Controller注解使得该类成为控制器  
Action注解来定义映射关系，映射书写规则为 Method:/path  
JsonResponse注解会将方法返回直接解析成为Json格式类型，或者在Conroller类上添加JsonResponse注解使得其所有的方法都返回Json

 ```java
 @Controller
public class Test {
	
	@Action("/hello.do")
	public String hello() {
		System.out.println("hello");
		return "hello";
    }
    
    @Action("POST:/getStudent")
    @JsonResponse
    public Student getStudent(){
        Student student = new Student("张三");
        return student;
    }

}
 ```
 AOP示例：  
 Aspect注解可以将指定类定义为代理类，一个代理类可以为多个目标类提供代理
 ```java
 /**
  * 拦截所有controller方法
  */
@Aspect(Controller.class)
public class ControllerAspect extend AspectProxy{
    private long begin;
    private long end;
    @Override
    public Object begin(Class<?> cls , Method method , Object[] params) {
        begin = System.currentTimeMillis();
        logger.debug("----------begin------------: {}" , begin);
		return null;
	}
	@Override
	public Object after(Class<?> cls , Method method , Object[] params , Object result) {
        end = System.currentTimeMillis();
        logger.debug("----------end------------: {}" , end);
        logger.debug("cost: {}" , end-begin)
		return null;
	}
}
 ```
