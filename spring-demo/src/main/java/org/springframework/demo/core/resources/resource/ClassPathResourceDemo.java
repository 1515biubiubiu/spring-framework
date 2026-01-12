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

package org.springframework.demo.core.resources.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.demo.core.resources.resource.support.DemonstrateResourceSupport;

/**
 * ClassPathResource demo。
 *
 * <p>
 * ClassPathResource 提供了基于 {@link ClassLoader} 和 {@link Class} 的构造方式。
 * 使用 {@link ClassLoader} 时，资源路径始终被视为 classpath 根路径下的绝对路径；
 * 使用 {@link Class} 时，Spring 会基于该类所在包对路径进行解析，
 * 从而支持类似 {@link Class#getResource(String)} 的相对路径语义。
 * </p>
 *
 * <ul>
 *   <li>不以 "/" 开头的路径：相对于当前类所在包</li>
 *   <li>以 "/" 开头的路径：相对于 classpath 根路径</li>
 * </ul>
 *
 * <p>
 * 虽然最终资源加载仍然委托给 {@link ClassLoader}，
 * 但 {@link Class} 构造方式在此之前额外提供了一层路径解析能力，
 * 这是两个构造方法行为差异的根本原因。
 * </p>
 *
 * @author dsy
 * @since 7.0
 */
public final class ClassPathResourceDemo {

	private static final Log log = LogFactory.getLog(ClassPathResourceDemo.class);

	private ClassPathResourceDemo() {}

	public static void main(String[] args) {
		classLoaderCtor();
		clazzCtor();
	}

	/**
	 * 使用 null ClassLoader 的构造函数实例化 ClassPathResource。
	 */
	private static void classLoaderCtor() {
		ClassPathResource resource = new ClassPathResource("resource/applicationContext.xml", ClassPathResourceDemo.class.getClassLoader());
		demonstrateResource(resource,"从 null ClassLoader 构造");
	}

	/**
	 * 使用 clazz 的构造函数实例化 ClassPathResource。
	 */
	private static void clazzCtor() {
		ClassPathResource resource = new ClassPathResource("applicationContext.xml", ClassPathResourceDemo.class);
		demonstrateResource(resource, "从 clazz 构造");
	}

	/**
	 * 演示 ClassPathResource 方法的使用。
	 * @param resource 要演示操作的 ClassPathResource 实例
	 */
	private static void demonstrateResource(ClassPathResource resource, String label) {
		DemonstrateResourceSupport.demonstrateResource(resource, label);

		log.info("路径：" + resource.getPath());
		log.info("类加载器：" + resource.getClassLoader());

		// ---------- 相等性比较 ----------
		ClassPathResource sameResource = new ClassPathResource(resource.getPath());
		log.info("Equals same URL: " + resource.equals(sameResource));
		log.info("HashCode: " + resource.hashCode());
	}

}
