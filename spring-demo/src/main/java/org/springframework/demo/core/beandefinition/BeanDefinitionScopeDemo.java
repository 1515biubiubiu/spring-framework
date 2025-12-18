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

package org.springframework.demo.core.beandefinition;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.GenericApplicationContext;

/**
 * BeanDefinition 中 scope 相关的示例。
 *
 * @author dsy
 * @since 7.0
 */
public class BeanDefinitionScopeDemo {

	private static final Log log = LogFactory.getLog(BeanDefinitionScopeDemo.class);

	public static void main(String[] args) {
		setScope();
		customScope();
	}

	/**
	 * 编程方式设置 scope。
	 */
	private static void setScope() {
		GenericApplicationContext context = new GenericApplicationContext();
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(MyService.class);
		// 设置为 prototype。
		bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		// 注册到容器。
		context.registerBeanDefinition("myService", bd);
		context.refresh();

		MyService bean = (MyService) context.getBean("myService");
		bean.hello();
		log.info(String.format("scope：%s", bd.getScope()));
	}

	/**
	 * 自定义 scope。
	 */
	private static void customScope() {
		GenericApplicationContext context = new GenericApplicationContext();
		// 注册自定义 scope。
		String customScope = "thread";
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		beanFactory.registerScope(customScope, new ThreadLocalScope());

		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(MyService.class);
		// 设置为 thread，如果设置为不存在的 scope getBean() 时会报错。
		bd.setScope(customScope);
		// 注册到容器。
		context.registerBeanDefinition("myService", bd);
		context.refresh();

		MyService bean = (MyService) context.getBean("myService");
		bean.hello();

		log.info(String.format("scope：%s", bd.getScope()));
	}

	@Bean
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)//注解方式设置 scope
	public MyService myService() {
		return new MyService();
	}

	public static class MyService {

		public void hello() {
			log.info("hello");
		}

	}

	/**
	 * 自定义的线程上下文 scope。
	 */
	public static class ThreadLocalScope implements org.springframework.beans.factory.config.Scope {

		private final ThreadLocal<Map<String, Object>> threadLocal = ThreadLocal.withInitial(HashMap::new);

		private final ThreadLocal<Map<String, Runnable>> destructionCallbacks = ThreadLocal.withInitial(HashMap::new);

		@Override
		public Object get(String name, ObjectFactory<?> objectFactory) {
			Map<String, Object> scope = this.threadLocal.get();
			return scope.computeIfAbsent(name, k -> objectFactory.getObject());
		}

		@Override
		public Object remove(String name) {
			this.destructionCallbacks.get().remove(name);
			return this.threadLocal.get().remove(name);
		}

		@Override
		public void registerDestructionCallback(String name, Runnable callback) {
			this.destructionCallbacks.get().put(name, callback);
		}

		@Override
		public Object resolveContextualObject(String key) {
			return null;
		}

		@Override
		public String getConversationId() {
			return Thread.currentThread().getName();
		}

		/**
		 * 手动清理线程的缓存。
		 */
		public void clear() {
			this.destructionCallbacks.get().values().forEach(Runnable::run);
			this.destructionCallbacks.remove();
			this.threadLocal.remove();
		}

	}

}
