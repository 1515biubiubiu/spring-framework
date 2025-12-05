package org.springframework.demo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dsy
 */
public class Test {
	public static void main(String[] args) {
		// 创建 Spring 上下文
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		// 注册配置类
		context.register(AppConfig.class);
		// 刷新上下文，使配置生效
		context.refresh();

		// 从上下文获取 Bean
		MyService myService = context.getBean(MyService.class);
		myService.sayHello();

		// 关闭上下文
		context.close();
	}

	@Configuration
	static class AppConfig {
		@Bean
		public MyService myService() {
			return new MyService();
		}
	}

	static class MyService {
		public void sayHello() {
			System.out.println("Hello, Spring!");
		}
	}
}
