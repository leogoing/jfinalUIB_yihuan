/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Before is used to configure Interceptor or Validator.
 */
/*表示可以被标注过的class的子类所继承*/
@Inherited
/*元注解-定义Annotation被保留的时间长短*/
@Retention(RetentionPolicy.RUNTIME/*在运行时有效（即运行时保留）*/)
/*元注解-定义Annotation所修饰的对象范围*/
@Target({ElementType.TYPE/*描述类、接口(包括注解类型) 或枚举声明*/, ElementType.METHOD/*描述方法*/})
public @interface Before {
	Class<? extends Interceptor>[] value();
}
