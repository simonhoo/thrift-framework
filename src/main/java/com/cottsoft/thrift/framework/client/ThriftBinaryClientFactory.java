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

package com.cottsoft.thrift.framework.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cottsoft.thrift.framework.exception.ThriftException;

/**
 * Description：<br> 
 * Thrfit客户端工厂
 * @author  Simon.Hoo(Info@cottsoft.com)
 * @date    2013年06月13日
 * @version v1.0.0
 */
public class ThriftBinaryClientFactory extends BaseClientFactory {

	private Logger logger = LoggerFactory.getLogger(ThriftBinaryClientFactory.class);
	
	public ThriftBinaryClientFactory(){
	}
	
	public ThriftBinaryClientFactory(String hostIp,int port,int timeout,String thriftService){
		super(hostIp,port,timeout,thriftService);
	}
	
	@Override
	public Object getClient() throws ThriftException {
		Object clientObject = null;
		try{			
			if(super.getTransport()==null){			
				super.init();
			}

			TProtocol protocol = new TBinaryProtocol(super.getTransport());
			
			TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,thriftService);
			
			Class<?> client =  Class.forName(thriftService+"$Client");
			Constructor<?> constructor = client.getConstructor(TProtocol.class);
			clientObject = constructor.newInstance(mp);
		} catch (SecurityException e) {
			if(logger.isInfoEnabled()){
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (NoSuchMethodException e) {
			if(logger.isInfoEnabled()){
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (IllegalArgumentException e) {
			if(logger.isInfoEnabled()){
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (InstantiationException e) {
			if(logger.isInfoEnabled()){
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (IllegalAccessException e) {
			if(logger.isInfoEnabled()){
				e.printStackTrace();
			}
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			if(logger.isInfoEnabled()){
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch (ClassNotFoundException e) {
			if(logger.isInfoEnabled()){
				e.printStackTrace();
			}
			throw new ThriftException(e);
		} catch(Exception e){
			if(logger.isInfoEnabled()){
				e.printStackTrace();
			}
			throw new ThriftException(e);
		}
		return clientObject;
	}

}


