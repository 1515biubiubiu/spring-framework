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

/**
 * 测试xml方式定义bean.
 *
 * @author sy d
 */
public class User {

	private Long id;

	private String name;

	private Integer age;

	public Long id() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String name() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer age() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("User{");
		sb.append("id=").append(this.id);
		sb.append(", name='").append(this.name).append('\'');
		sb.append(", age=").append(this.age);
		sb.append('}');
		return sb.toString();
	}
}