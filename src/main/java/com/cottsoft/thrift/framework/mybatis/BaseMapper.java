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

package com.cottsoft.thrift.framework.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Description：<br> 
 * 通用Mapper接口
 * @author  Simon.Hoo(Info@cottsoft.com)
 * @date    2014年12月13日
 * @version v1.0.0
 */
public interface BaseMapper<T, PK> {
	/**
	 * 添加对象
	 * @param o
	 * @return
	 */
	public int insert(T o);
	
	/**
	 * 根据主键删除对象
	 * @param pk
	 * @return
	 */
	public int deleteByPrimaryKey(PK pk);
	
	/**
	 * 根据条件删除对象
	 * @param params
	 * @return
	 */
	public int deleteByMap(Map<String, Object> params);
	
	/**
	 * 删除对象
	 * @param o
	 * @return
	 */
	public int delete(T o);

	/**
	 * 根据主键查询对象
	 * @param pk
	 * @return
	 */
	public T selectByPrimaryKey(PK pk);
	
	/**
	 * 根据条件查询对象列表
	 * @param params
	 * @return
	 */
	public List<T> selectByMap(Map<String, Object> params);
	
	/**
	 * 查询所有对象
	 * @return
	 */
	public List<T> selectAll();
	
	
	/**
	 * 查询所有记录总数
	 * @return
	 */
	public Long selectAllCnt();
	
	
	
	/**
	 * 根据主键更新对象
	 * @param o
	 * @return
	 */
	public int updateByPrimaryKey(T o);

	/**
	 * 根据条件更新对象
	 * @param o
	 * @param params
	 * @return
	 */
	public int updateWithMap(T o,Map<String, Object> params);
}


