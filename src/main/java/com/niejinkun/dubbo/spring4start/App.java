package com.niejinkun.dubbo.spring4start;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.niejinkun.dubbo.spring4start.config.DubboConfig;
import com.niejinkun.dubbo.spring4start.service.DemoService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	AbstractApplicationContext context = new AnnotationConfigApplicationContext(DubboConfig.class);
    	
    	@SuppressWarnings("unchecked")
		ReferenceConfig<DemoService> reference = (ReferenceConfig<DemoService>) context.getBean("reference");
    	
    	DemoService service = reference.get();
    	
    	//  DemoService service = (DemoService)context.getBean("demoService"); // 获取远程服务代理
    	String hello = service.sayHello("nihao");
    	
    	System.out.println(hello);
    	context.start();
    	

    	
    }
    			
}
