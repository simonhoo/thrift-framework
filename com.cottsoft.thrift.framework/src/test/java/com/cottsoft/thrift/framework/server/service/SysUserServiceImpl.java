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

package com.cottsoft.thrift.framework.server.service;

import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cottsoft.thrift.framework.annotation.ThriftService;
import com.cottsoft.thrift.framework.rpc.common.ThriftServiceException;
import com.cottsoft.thrift.framework.rpc.user.SysUser;
import com.cottsoft.thrift.framework.rpc.user.SysUserService.Iface;

/**
 * Description：<br> 
 * 
 * @author  Simon.Hoo(Info@cottsoft.com)
 * @date    2014年12月13日
 * @version v1.0.0
 */
@Service("sysUserServiceImpl")
@ThriftService(service = "com.cottsoft.thrift.framework.rpc.user.SysUserService")
public class SysUserServiceImpl implements Iface {

	@Override
	public int addSysUser(SysUser sysUser) throws ThriftServiceException,
			TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteSysUser(SysUser sysUser) throws ThriftServiceException,
			TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSysUser(SysUser sysUser) throws ThriftServiceException,
			TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SysUser querySysUserByName(String userName) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysUser> getSysUserList() throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysUser> getSysUserByParameter(Map<String, String> parameter)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}

}


