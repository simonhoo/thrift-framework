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

package com.cottsoft.thrift.framework.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description：<br> 
 * Thrift 服务端启动
 * @author  Simon.Hoo(Info@cottsoft.com)
 * @date    2014年12月13日
 * @version v1.0.0
 */
public class RunServer extends Thread {
	private Logger logger = LoggerFactory.getLogger(RunServer.class);
	
	public static void main(String[] args) {
		Thread thread = new RunServer();
		thread.start();
	}
	
	@Override
	public void run() {
		try{
			new ClassPathXmlApplicationContext(new String[] {"config/app.xml"});
		} catch (SecurityException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


