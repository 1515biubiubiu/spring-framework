/*
 * Copyright 2002-2020 the original author or authors.
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


package org.springframework.demo.container.xml;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;

/**
 * @author dsy
 */
public class XMLContainerTests {

	@Test
	public void test1() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-bean.xml");
		Object user = context.getBean("user");
		System.out.println(user);
	}

	@Test
	public void test2() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-service.xml", "spring-dao.xml");
		Object petStoreService = context.getBean("petStoreService");
		System.out.println(petStoreService);
	}

	@Test
	public void test3() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		Object user = context.getBean("user");
		System.out.println(user);
	}

	@Test
	public void test4() {
		GenericGroovyApplicationContext context = new GenericGroovyApplicationContext("classpath:beans.groovy");
		Object user = context.getBean(User.class);
		System.out.println(user);
	}

	@Test
	public void test5() {
		GenericApplicationContext context = new GenericApplicationContext();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions("spring.xml");
		context.refresh();
		Object user = context.getBean("user");
		System.out.println(user);
	}

	@Test
	public void test6() {
		GenericApplicationContext context = new GenericApplicationContext();
		GroovyBeanDefinitionReader reader = new GroovyBeanDefinitionReader(context);
		reader.loadBeanDefinitions("beans.groovy");
		context.refresh();
		Object user = context.getBean("user");
		System.out.println(user);
	}
}