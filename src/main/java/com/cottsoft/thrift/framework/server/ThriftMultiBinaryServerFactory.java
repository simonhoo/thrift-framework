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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cottsoft.thrift.framework.annotation.ThriftService;
import com.cottsoft.thrift.framework.context.SpringApplicationContext;
import com.cottsoft.thrift.framework.exception.ThriftException;
import com.cottsoft.thrift.framework.hander.ScanPackageHander;

/**
 * Description：<br> 
 * Thrift服务端工厂
 * @author  Simon.Hoo(Info@cottsoft.com)
 * @date    2013年06月13日
 * @version v1.0.0
 */
public class ThriftMultiBinaryServerFactory {
	private Logger logger = LoggerFactory.getLogger(ThriftMultiBinaryServerFactory.class);

	/**
	 * 服务端TCP端口
	 */
	private Integer port = 9999;

	/**
	 * Thrift服务端实现基础包名
	 */
	private String baseServiceImplPackage;

	/**
	 * 超时时长
	 */
	private Integer timeout = 2000;


	public ThriftMultiBinaryServerFactory() {
	}

	public ThriftMultiBinaryServerFactory(String baseServiceImplPackage) {
		this.baseServiceImplPackage = baseServiceImplPackage;
	}

	public ThriftMultiBinaryServerFactory(String baseServiceImplPackage, Integer port) {
		this.baseServiceImplPackage = baseServiceImplPackage;
		this.port = port;
	}

	public ThriftMultiBinaryServerFactory(String baseServiceImplPackage, Integer port, Integer timeout) {
		this.baseServiceImplPackage = baseServiceImplPackage;
		this.port = port;
		this.timeout = timeout;
	}

	/**
	 * 启动服务
	 * 
	 * @return
	 */
	public int start() {
		int result = 0;
		try {
			TServer server = getServer();
			server.serve();
			result = 1;
			System.out.println("Starting server on port " + port + " ...");
		} catch (ThriftException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取服务端Socket连接
	 * 
	 * @return
	 * @throws ThriftException
	 */
	public TServerTransport getServerTransport() throws ThriftException {
		try {
			if (timeout > 0) {
				return new TServerSocket(port, timeout);
			} else {
				return new TServerSocket(port);
			}
		} catch (TTransportException e) {
			e.printStackTrace();
			throw new ThriftException(e);
		}
	}

	/**
	 * 获取Processor实例
	 * @return
	 * @throws ThriftException
	 */
	public TProcessor getProcessor() throws ThriftException {
		try {
			TMultiplexedProcessor processor = new TMultiplexedProcessor();

			Set<Class<?>> thriftServiceImplClassList = ScanPackageHander.getPackageAllClasses(baseServiceImplPackage, true);

			for (Class<?> thriftServiceImplClass : thriftServiceImplClassList) {
				if (thriftServiceImplClass.isAnnotationPresent(ThriftService.class)) {
					ThriftService thriftServiceAnnotation = (ThriftService) thriftServiceImplClass.getAnnotation(ThriftService.class);
					String thriftServiceName = thriftServiceAnnotation.service();

					Constructor<?> constructor = Class.forName(thriftServiceName + "$Processor").getConstructor(Class.forName(thriftServiceName + "$Iface"));

					Object service = SpringApplicationContext.getBean(getServiceImplBeanName(thriftServiceImplClass));

					processor.registerProcessor(thriftServiceName, (TProcessor) constructor.newInstance(service));

					if (logger.isDebugEnabled()) {
						logger.debug(">>> Thrift Service implements class: " + thriftServiceImplClass.getName());
					}
				}
			}
			return processor;
		} catch (NoSuchMethodException e) {
			if (logger.isInfoEnabled()) {
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (SecurityException e) {
			if (logger.isInfoEnabled()) {
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (ClassNotFoundException e) {
			if (logger.isInfoEnabled()) {
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (InstantiationException e) {
			if (logger.isInfoEnabled()) {
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (IllegalAccessException e) {
			if (logger.isInfoEnabled()) {
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (IllegalArgumentException e) {
			if (logger.isInfoEnabled()) {
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (InvocationTargetException e) {
			if (logger.isInfoEnabled()) {
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				e.printStackTrace();
			}
			throw new ThriftException(e);
		}
	}

	/**
	 * 获取Service impl的Spring bean名称，首先根据@Service注解来判断，如果没有则为类的间单名
	 * 
	 * @param thriftServiceImplClass
	 * @return
	 */
	private String getServiceImplBeanName(Class<?> thriftServiceImplClass) {
		if (thriftServiceImplClass.isAnnotationPresent(Service.class)) {
			Service serviceAnnotation = (Service) thriftServiceImplClass.getAnnotation(Service.class);
			String value = serviceAnnotation.value();

			if (StringUtils.isEmpty(value)) {
				return StringUtils.uncapitalize(thriftServiceImplClass.getSimpleName());
			} else {
				return value;
			}
		} else {
			return StringUtils.uncapitalize(thriftServiceImplClass.getSimpleName());
		}
	}

	/**
	 * 获取协议工厂
	 * 
	 * @return
	 * @throws ThriftException
	 */
	public TProtocolFactory getProtocolFactory() throws ThriftException {
		return new TBinaryProtocol.Factory(true, true);
	}

	/**
	 * 获取服务器
	 * 
	 * @return
	 * @throws ThriftException
	 */
	public TServer getServer() throws ThriftException {
		return new TThreadPoolServer(new TThreadPoolServer.Args(getServerTransport()).protocolFactory(getProtocolFactory()).processor(getProcessor()));
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getBaseServiceImplPackage() {
		return baseServiceImplPackage;
	}

	public void setBaseServiceImplPackage(String baseServiceImplPackage) {
		this.baseServiceImplPackage = baseServiceImplPackage;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
}


