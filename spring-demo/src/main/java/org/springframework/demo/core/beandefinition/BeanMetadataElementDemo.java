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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * BeanMetadataElement Demo。
 *
 * @author dsy
 * @since 7.0
 */
@Configuration
public class BeanMetadataElementDemo {

	private static final Log log = LogFactory.getLog(BeanMetadataElementDemo.class);

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(BeanMetadataElementDemo.class);

		BeanDefinition bd = ctx.getBeanFactory().getBeanDefinition("myService");
		Object source = bd.getSource();
		log.info("Source = " + source);
		log.info("Source class = " + (source != null ? source.getClass() : "null"));

		ctx.close();
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
