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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.ClassPathResource;

/**
 * ClassPathResource demo。
 * @author dsy
 * @since 7.0
 */
public final class ClassPathResourceDemo {

	private static final Log log = LogFactory.getLog(ClassPathResourceDemo.class);

	private ClassPathResourceDemo() {}

	public static void main(String[] args) throws FileNotFoundException {
		ClassPathResource resource = new ClassPathResource("resource/applicationContext.xml");
		log.info("exists: " + resource.exists());
		log.info("description: " + resource.getDescription());

		try (InputStream in = resource.getInputStream()) {
			// 读取内容示例
			byte[] all = in.readAllBytes();
			String content = new String(all, StandardCharsets.UTF_8);
			log.info("内容:\n" + content);
		}
		catch (IOException ex) {
			log.error("读取失败", ex);
		}
	}

}
