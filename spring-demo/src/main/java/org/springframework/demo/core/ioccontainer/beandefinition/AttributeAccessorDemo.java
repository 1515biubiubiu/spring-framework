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

package org.springframework.demo.core.ioccontainer.beandefinition;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AttributeAccessor 接口在 BeanDefinition 中使用示例。
 * @author dsy
 * @since 7.0
 */
@Configuration
public class AttributeAccessorDemo {

	private static final Log log = LogFactory.getLog(AttributeAccessorDemo.class);

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(BeanMetadataElementDemo.class);

		BeanDefinition bd = ctx.getBeanFactory().getBeanDefinition("myService");
		// 设置属性
		bd.setAttribute("test", "test1");
		// 使用 computeAttribute
		Object lazyValue = bd.computeAttribute("lazyAttr", name -> "computedValue");

		// 输出日志
		log.info(String.format("Attribute 'test': %s", bd.getAttribute("test")));
		log.info(String.format("Attribute 'test2' (not set): %s", bd.getAttribute("test2")));
		log.info(String.format("Attribute 'lazyAttr' computed value: %s", lazyValue));
		log.info(String.format("All attribute names: %s", Arrays.asList(bd.attributeNames())));
	}

	@Bean
	public MyService myService() {
		return new MyService();
	}

	public static class MyService {

		public void hello() {
			log.info("hello");
		}

	}

}
