package com.niejinkun.dubbo.spring4start.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.annotation.Reference;
import com.niejinkun.dubbo.spring4start.service.DemoService;

@Configuration
public class DubboConfig {

	// 当前应用配置
	@Bean
	public ApplicationConfig application() {
		ApplicationConfig application = new ApplicationConfig();
		application.setName("demoService-comsumer");

		return application;
	}

	// 连接注册中心
	@Bean
	public RegistryConfig registry() {
		RegistryConfig registry = new RegistryConfig();
		registry.setProtocol("zookeeper");
		registry.setAddress("zookeeper://127.0.0.1:2181");
/*		registry.setUsername("aaa");
		registry.setPassword("bbb");*/
		return registry;
	}
	
	// 服务提供者协议配置
	@Bean
	public ProtocolConfig protocol(){
	ProtocolConfig protocol = new ProtocolConfig();
	protocol.setName("dubbo");
	protocol.setPort(20880);
	protocol.setThreads(200);
	
	return protocol;
	}
	// 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
	 
/*	// 服务提供者暴露服务配置
	@Bean
	public ServiceConfig<DemoService> service(){
	ServiceConfig<DemoService> service = new ServiceConfig<DemoService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
	service.setApplication(application());
	service.setRegistry(registry()); // 多个注册中心可以用setRegistries()
	service.setProtocol(protocol()); // 多个协议可以用setProtocols()
	service.setInterface(DemoService.class);
	service.setVersion("1.0.0");
	
	return service;
	}*/
	
	// 服务引用　配置
	@Bean
	public ReferenceConfig<DemoService> reference(){
		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>();
		reference.setApplication(application());
		reference.setRegistry(registry());
		reference.setProtocol("dubbo");
		reference.setInterface(DemoService.class);
		reference.setId("demoService");
		reference.setVersion("1.0.0");   // 这行必须要有
		
		return reference;
		
	}
	
/*	@Bean
	public DemoService demoService(){
		return reference().get();
	}
	 */

}
