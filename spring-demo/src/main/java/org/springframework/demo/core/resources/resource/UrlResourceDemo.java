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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.UrlResource;
import org.springframework.demo.core.resources.resource.support.DemonstrateResourceSupport;

/**
 * UrlResource 使用演示。
 *
 * <p>
 * UrlResource 支持多种 URL 协议（file、http、https、jar、war、ftp 等），
 * 并提供了统一的资源访问接口。核心特性包括：
 * </p>
 *
 * <ul>
 *   <li><strong>多协议支持</strong>：通过标准的 {@link URL} 机制支持各种协议</li>
 *   <li><strong>身份认证集成</strong>：自动处理 URL 中的用户凭据（user:password@host）</li>
 *   <li><strong>缓存控制</strong>：可细粒度控制 URLConnection 的缓存行为</li>
 * </ul>
 *
 * <p>
 * 构造方式说明：
 * </p>
 * <ol>
 *   <li><strong>URL 构造</strong>：直接使用 {@link URL} 对象，适合已存在的 URL 实例</li>
 *   <li><strong>URI 构造（推荐）</strong>：使用 {@link URI} 对象，支持更严格的格式验证</li>
 *   <li><strong>路径构造</strong>：使用路径字符串，自动选择 URI 或 URL 解析方式</li>
 *   <li><strong>组件构造</strong>：分别指定协议、位置和片段，适合动态构建</li>
 * </ol>
 *
 * <p>
 * 缓存控制策略：
 * </p>
 * <ul>
 *   <li><strong>默认策略</strong>：null 表示使用默认（通常只对 jar 资源启用缓存）</li>
 *   <li><strong>显式启用</strong>：true 表示对所有资源启用缓存</li>
 *   <li><strong>显式禁用</strong>：false 表示对所有资源禁用缓存</li>
 * </ul>
 *
 * @author dsy
 * @since 7.0
 */
public final class UrlResourceDemo {

	private static final Log log = LogFactory.getLog(UrlResourceDemo.class);

	private UrlResourceDemo() {
	}

	public static void main(String[] args) {
		demonstrateFileProtocol();
		demonstrateHttpProtocol();
		demonstrateAuthentication();
		demonstrateCachingControl();
	}

	/**
	 * 演示 file:// 协议的资源访问。
	 */
	private static void demonstrateFileProtocol() {
		log.info("========== File Protocol Demo ==========");

		// 当前工程工作目录
		Path projectRoot = Path.of(System.getProperty("user.dir"));
		// 目标 java 文件目录
		Path javaFileDir = projectRoot.resolve("spring-demo/src/main/java");
		try {
			// 方式1: 从文件路径构造（自动转换为 file:// URL）
			Path resolve = javaFileDir.resolve("org/springframework/demo/core/resources/resource/UrlResourceDemo.java");
			String fileStr = "file:" + resolve.toAbsolutePath();
			UrlResource fileResource1 = new UrlResource(fileStr);

			// 方式2: 从 File 对象构造
			File file = new File(resolve.toString());
			UrlResource fileResource2 = new UrlResource(file.toURI().toURL());

			// 方式3: 使用静态工厂方法（异常包装）
			UrlResource fileResource3 = UrlResource.from(fileStr);

			demonstrateResource(fileResource1, "从文件路径构造");
			demonstrateResource(fileResource2, "从 File 对象构造");
			demonstrateResource(fileResource3, "使用静态工厂方法（异常包装）");

		}
		catch (MalformedURLException ex) {
			log.error("File protocol demo failed", ex);
		}
	}

	/**
	 * 演示 http:// 协议的资源访问。
	 */
	private static void demonstrateHttpProtocol() {
		log.info("\n========== HTTP Protocol Demo ==========");
		// 访问 Spring 官网
		try {
			UrlResource httpResource = new UrlResource("https://spring.io");
			demonstrateResource(httpResource, "使用 HTTP Protocol 构造");
		}
		catch (MalformedURLException ex) {
			log.error("HTTP protocol demo failed", ex);
		}

	}

	/**
	 * 演示带身份认证的资源访问。
	 */
	private static void demonstrateAuthentication() {
		log.info("\n========== Authentication Demo ==========");

		// 注意：实际使用时需要有效的凭据
		String authUrl = "https://user:pass@httpbin.org/basic-auth/user/pass";
		UrlResource authResource = null;
		try {
			authResource = new UrlResource(authUrl);
		}
		catch (MalformedURLException ex) {
			log.error("Invalid URL", ex);
			return;
		}

		try (InputStream in = authResource.getInputStream()) {
			byte[] buffer = new byte[256];
			int len;
			StringBuilder content = new StringBuilder();
			while ((len = in.read(buffer)) != -1) {
				content.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
			}
			log.info("Successfully authenticated and read resource: " + content);
		}
		catch (IOException ex) {
			// 当用户名或密码错误时：Authentication failed: Server returned HTTP response code: 401 for URL: https://user:？@httpbin.org/basic-auth/user/pass
			log.warn("Authentication failed: " + ex.getMessage());
		}
	}

	/**
	 * 演示缓存控制功能。
	 */
	private static void demonstrateCachingControl() {
		log.info("\n========== Caching Control Demo ==========");

		try {
			// 创建资源并设置缓存策略
			UrlResource resource = new UrlResource("https://spring.io"){
				@Override
				public InputStream getInputStream() throws IOException {
					URLConnection con = this.getURL().openConnection();
					customizeConnection(con);
					try {
						// 重写这个方法，仅加了这一行日志打印。其他逻辑完全不变。
						log.info("caching: " + con.getUseCaches());
						return con.getInputStream();
					}
					catch (IOException ex) {
						// Close the HTTP connection (if applicable).
						if (con instanceof HttpURLConnection httpCon) {
							httpCon.disconnect();
						}
						throw ex;
					}
				}
			};
			// 默认缓存策略
			resource.getInputStream();
			// 显式启用缓存
			resource.setUseCaches(true);
			resource.getInputStream();
			// 显式禁用缓存
			resource.setUseCaches(false);
			resource.getInputStream();
		}
		catch (IOException ex) {
			log.error("Caching control demo failed", ex);
		}
	}

	/**
	 * 演示 UrlResource 的核心功能。
	 * @param resource 要演示的 UrlResource 实例
	 * @param label 资源标签（用于日志输出）
	 */
	private static void demonstrateResource(UrlResource resource, String label) {
		DemonstrateResourceSupport.demonstrateResource(resource, label);

		// ---------- 相等性比较 ----------
		UrlResource sameResource = new UrlResource(resource.getURL());
		log.info("Equals same URL: " + resource.equals(sameResource));
		log.info("HashCode: " + resource.hashCode());

	}

}
