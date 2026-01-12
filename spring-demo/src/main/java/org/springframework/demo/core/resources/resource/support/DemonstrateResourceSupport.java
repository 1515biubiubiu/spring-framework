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

package org.springframework.demo.core.resources.resource.support;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.Resource;

/**
 * 演示 Resource 的核心功能。
 * @author dsy
 * @since 7.0
 */
public abstract class DemonstrateResourceSupport {

	private static final Log log = LogFactory.getLog(DemonstrateResourceSupport.class);

	private DemonstrateResourceSupport() {}

	/**
	 * 演示 Resource 的核心功能。
	 * @param resource 要演示的 Resource 实例
	 * @param label 资源标签（用于日志输出）
	 */
	public static void demonstrateResource(Resource resource, String label) {
		log.info("\n--- " + label + " ---");
		log.info("Resource Type: " + resource.getClass().getSimpleName());

		// ---------- 存在性与可读性判断 ----------
		log.info("Exists: " + resource.exists());
		log.info("Readable: " + resource.isReadable());
		log.info("Open: " + resource.isOpen());
		log.info("File protocol: " + resource.isFile());

		// ---------- 获取资源内容 ----------
		if (resource.exists() && resource.isReadable()) {
			log.info("Reading InputStream...");
			try (InputStream in = resource.getInputStream()) {
				byte[] buffer = new byte[200];
				int read = in.read(buffer);
				if (read > 0) {
					String part = new String(buffer, 0, read, StandardCharsets.UTF_8);
					log.info("First 200 chars from inputStream: " + part.replace("\n", "\\n"));
				}
				else {
					log.info("No content read via InputStream");
				}
			}
			catch (IOException ex) {
				log.error("Failed to read inputStream", ex);
			}
			log.info("Finished reading InputStream");

			log.info("Reading channel...");
			try (ReadableByteChannel channel = resource.readableChannel()) {
				ByteBuffer buffer = ByteBuffer.allocate(200);
				int bytesRead = channel.read(buffer);
				if (bytesRead > 0) {
					buffer.flip();
					byte[] bytes = new byte[buffer.remaining()];
					buffer.get(bytes);
					String part = new String(bytes, StandardCharsets.UTF_8);
					log.info("First 200 chars from channel: " + part.replace("\n", "\\n"));
				}
				else {
					log.info("No content read via channel");
				}
			}
			catch (IOException ex) {
				log.error("Failed to read channel", ex);
			}
			log.info("Finished reading channel");

			try {
				byte[] contentArr = resource.getContentAsByteArray();
				int len = Math.min(contentArr.length, 200);
				byte[] partArr = Arrays.copyOf(contentArr, len);
				log.info("content byte array (first " + len + " bytes): " + Arrays.toString(partArr));
			}
			catch (IOException ex) {
				log.error("Failed to read content byte array", ex);
			}

			try {
				String contentStr = resource.getContentAsString(Charset.defaultCharset());
				String part = contentStr.length() <= 200 ? contentStr : contentStr.substring(0, 200);
				log.info("content string (first 200 chars): " + part.replace("\n", "\\n"));
			}
			catch (IOException ex) {
				log.error("Failed to read content string", ex);
			}
		}

		// ---------- 资源定位 ----------
		try {
			log.info("URL: " + resource.getURL());
			log.info("URI: " + resource.getURI());
		}
		catch (IOException ex) {
			log.warn("Failed to get URL/URI: " + ex.getMessage(), ex);
		}

		try {
			log.info("File: " + resource.getFile());
			log.info("FilePath: " + resource.getFilePath());
		}
		catch (IOException ex) {
			log.warn("Failed to get File/FilePath: ", ex);
		}

		// ---------- 元数据 ----------
		try {
			log.info("Content length: " + resource.contentLength());
		}
		catch (IOException ex) {
			log.warn("Failed to get content length: " + ex.getMessage(), ex);
		}

		try {
			long lastModified = resource.lastModified();
			log.info("Last modified: " +
					(lastModified > 0 ? new Date(lastModified) : "unknown"));
		}
		catch (IOException ex) {
			log.warn("Failed to get last modified time: " + ex.getMessage(), ex);
		}

		log.info("Filename: " + resource.getFilename());
		log.info("Description: " + resource.getDescription());


		// ---------- 通过相对路径创建 Resource ----------
		try {
			Resource relative = resource.createRelative("relative-test.txt");
			log.info("relative resource 描述：" + relative.getDescription());
			log.info("relative resource 是否存在：" + relative.exists());
		}
		catch (IOException ex) {
			log.info("create relative fail: " + ex.getMessage(), ex);
		}

	}

}
