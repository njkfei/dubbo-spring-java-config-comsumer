# dubbo-spring-java-config-comsumer

##　说明
有几个坑
### spring 依赖问题
 在dubbo依赖于spring2,如果集成spring3会有依赖冲突问题,需要添加依赖解决．
```
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>dubbo</artifactId>
			<version>2.5.3</version>
			        <exclusions>  
                <exclusion>  
                    <artifactId>spring</artifactId>  
                    <groupId>org.springframework</groupId>  
                </exclusion>  
            </exclusions>  
		</dependency
```

### 版本不一致的问题
如果服务端(服务提供者设置了版本信息)，则需要保持版本一致
```
	// 服务提供者暴露服务配置
	@Bean
	public ServiceConfig<DemoService> service(){
	ServiceConfig<DemoService> service = new ServiceConfig<DemoService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
	service.setApplication(application());
	service.setRegistry(registry()); // 多个注册中心可以用setRegistries()
	service.setProtocol(protocol()); // 多个协议可以用setProtocols()
	service.setInterface(DemoService.class);
	service.setVersion("1.0.0");   // 服务版本
	service.setRef(demoService());
	service.export();
	
	return service;
	}
```

```
	// 服务引用　配置,需要与服务提供者版本保持一致
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
```

如果不一致，会报下面的错误
```
Exception in thread "main" java.lang.IllegalStateException: Failed to check the status of the service com.niejinkun.dubbo.spring4start.service.DemoService. No provider available for the service com.niejinkun.dubbo.spring4start.service.DemoService from the url zookeeper://127.0.0.1:2181/com.alibaba.dubbo.registry.RegistryService?application=demoService-comsumer&dubbo=2.5.3&interface=com.niejinkun.dubbo.spring4start.service.DemoService&methods=sayHello&pid=2432&protocol=dubbo&side=consumer&timestamp=1457964636762 to the consumer 192.168.1.236 use dubbo version 2.5.3
	at com.alibaba.dubbo.config.ReferenceConfig.createProxy(ReferenceConfig.java:420)
	at com.alibaba.dubbo.config.ReferenceConfig.init(ReferenceConfig.java:300)
	at com.alibaba.dubbo.config.ReferenceConfig.get(ReferenceConfig.java:138)
	at com.niejinkun.dubbo.spring4start.App.main(App.java:24)

```

## 完整配置代码
```
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
	
	// 服务引用　配置,需要与服务提供者版本保持一致
	@Bean
	public ReferenceConfig<DemoService> reference(){
		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>();
		reference.setApplication(application());
		reference.setRegistry(registry());
		reference.setProtocol("dubbo");
		reference.setInterface(DemoService.class);
		reference.setId("demoService");
		//reference.setVersion("1.0.0");   // 这行必须要有
		
		return reference;
		
	}
	
/*	@Bean
	public DemoService demoService(){
		return reference().get();
	}
	 */

}

```
