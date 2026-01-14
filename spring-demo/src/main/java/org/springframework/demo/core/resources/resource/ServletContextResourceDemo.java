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

import org.springframework.demo.core.resources.resource.support.DemonstrateResourceSupport;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.ServletContextResource;

/**
 * {@link ServletContextResource} 示例类，用于演示如何通过 ServletContext 加载资源。
 * <p>
 * ServletContextResource 是 Spring 中 {@link org.springframework.core.io.ContextResource} 的实现，
 * 它可以通过 ServletContext 来访问 web 应用下的资源（如 WEB-INF 下的配置文件）。
 * <p>
 * 本示例使用 {@link MockServletContext} 模拟 ServletContext 环境，方便在普通 Java 应用中演示。
 * @author dsy
 * @since 7.0
 */
public final class ServletContextResourceDemo {

	private static final Log log = LogFactory.getLog(ServletContextResourceDemo.class);

	private ServletContextResourceDemo() {}

	public static void main(String[] args) {
		ServletContextResource resource = new ServletContextResource(new MockServletContext(), "resource/applicationContext.xml");
		demonstrateResource(resource, "ServletContextResource");
	}

	/**
	 * 演示 ServletContextResource 的核心功能。
	 * @param resource 要演示的 ServletContextResource 实例
	 * @param label 资源标签（用于日志输出）
	 */
	private static void demonstrateResource(ServletContextResource resource, String label) {
		DemonstrateResourceSupport.demonstrateResource(resource, label);
		// ---------- ServletContext 相关方法 ----------
		log.info("ServletContext 类型: " + resource.getServletContext().getClass().getName());
		log.info("ServletContext resource path: " + resource.getPath());
		log.info("ServletContext resource path within context: " + resource.getPathWithinContext());


		// ---------- 相等性比较 ----------
		// ServletContextResource 在比较时会对路径进行规范化（如 ../）
		ServletContextResource sameResource = new ServletContextResource(resource.getServletContext(),
				"resource/../resource/applicationContext.xml");
		log.info("Equals same URL: " + resource.equals(sameResource));
		log.info("HashCode: " + resource.hashCode());

	}

}
