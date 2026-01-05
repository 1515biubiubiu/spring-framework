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

package org.springframework.demo.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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
		demonstrateResource(resource);
	}

	/**
	 * 使用 clazz 的构造函数实例化 ClassPathResource。
	 */
	private static void clazzCtor() {
		ClassPathResource resource = new ClassPathResource("applicationContext.xml", ClassPathResourceDemo.class);
		demonstrateResource(resource);
	}

	/**
	 * 演示 ClassPathResource 方法的使用。
	 * @param resource 要演示操作的 ClassPathResource 实例
	 */
	private static void demonstrateResource(ClassPathResource resource) {
		log.info("========== ClassPathResource Demo ==========");

		// ---------- 存在性与可读性判断 ----------
		log.info("是否存在：" + resource.exists());
		log.info("是否可读：" + resource.isReadable());
		log.info("是否打开：" + resource.isOpen());
		log.info("是否是文件：" + resource.isFile());

		// ---------- 获取资源内容 ----------
		try (InputStream in = resource.getInputStream()) {
			// 读取内容示例
			byte[] all = in.readAllBytes();
			String content = new String(all, StandardCharsets.UTF_8);
			log.info("inputStream 读取内容：\n" + content);
		}
		catch (IOException ex) {
			log.error("读取 inputStream 失败", ex);
		}

		log.info("channel 读取内容：");
		try (ReadableByteChannel channel = resource.readableChannel()) {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (channel.read(buffer) != -1) {
				buffer.flip();
				byte[] bytes = new byte[buffer.remaining()];
				buffer.get(bytes);
				log.info(new String(bytes, StandardCharsets.UTF_8));
				buffer.clear();
			}
		}
		catch (IOException ex) {
			log.error("读取 channel 失败", ex);
		}

		try {
			byte[] contentArr = resource.getContentAsByteArray();
			log.info("内容字节数组：" + Arrays.toString(contentArr));
		}
		catch (IOException ex) {
			log.error("读取内容字节数组失败", ex);
		}

		try {
			String contentStr = resource.getContentAsString(Charset.defaultCharset());
			log.info("内容字符串：\n" + contentStr);
		}
		catch (IOException ex) {
			log.error("读取内容字符串失败", ex);
		}

		// ---------- 资源定位 ----------
		try {
			log.info("URL：" + resource.getURL());
			log.info("URI：" + resource.getURI());
			log.info("文件：" + resource.getFile());
			log.info("文件路径：" + resource.getFilePath());
		}
		catch (IOException ex) {
			log.warn("URL / URI / File / Path 获取失败", ex);
		}


		// ---------- 元信息 ----------
		try {
			log.info("内容长度：" + resource.contentLength());
			log.info("最后修改时间：" + resource.lastModified());
		}
		catch (IOException ex) {
			log.warn("内容长度 / 最后修改时间 获取失败", ex);
		}
		log.info("文件名：" + resource.getFilename());
		log.info("描述：" + resource.getDescription());
		log.info("路径：" + resource.getPath());
		log.info("类加载器：" + resource.getClassLoader());

		// ---------- 通过相对路径创建 Resource ----------
		Resource relative = resource.createRelative("relative-test.txt");
		log.info("relative resource 描述：" + relative.getDescription());
		log.info("relative resource 是否存在：" + relative.exists());
	}

}
