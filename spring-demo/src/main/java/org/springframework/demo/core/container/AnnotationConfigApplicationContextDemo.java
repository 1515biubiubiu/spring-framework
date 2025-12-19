/*
 * Copyright 2002-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.demo.core.container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * AnnotationConfigApplicationContext（注解驱动的容器）使用示例。
 * @author dsy
 * @since 7.0
 */
public final class AnnotationConfigApplicationContextDemo {

	private static final Log log = LogFactory.getLog(AnnotationConfigApplicationContextDemo.class);

	private AnnotationConfigApplicationContextDemo() {}

	public static void main(String[] args) {
		// 创建注解驱动的 Spring IoC 容器。
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		// 注册配置类。
		context.register(AppConfig.class);
		// 刷新容器。
		context.refresh();
		// 获取 Bean 并调用方法。
		MyService myService = context.getBean(MyService.class);
		myService.doSomething();

		// 关闭容器。
		context.close();
	}

}





