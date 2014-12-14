/*
 * Copyright 2005-2014 the original author or authors.
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
 * 
 * Author:	Simon.Hoo
 * Blog:   	http://www.cottsoft.com 
 * Email:	Info@cottsoft.com
 * 
 * You can @simonhoo  on Github.com, weibo.com, twitter, t.qq.com
 */

package com.cottsoft.thrift.framework.util;

/**
 * Description：<br> 
 * 对象操作工具
 * @author  Simon.Hoo(Info@cottsoft.com)
 * @date    2009年04月15日
 * @version v1.0.0
 */
public class ObjectUtil {

	/**
	 * 如前传入的对象t为null，则通过返映实例货传入的类型实例
	 * @param c
	 * @param t
	 * @return
	 */
	public static <T> T getNotNullObject(Class<T> c,T t){
		if(t==null){
			try {
				t = c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return t;
	}
}


