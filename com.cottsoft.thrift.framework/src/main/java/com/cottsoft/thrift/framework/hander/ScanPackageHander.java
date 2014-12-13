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

package com.cottsoft.thrift.framework.hander;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description：<br> 
 * 扫描包下面的java文件
 * @author  Simon.Hoo(Info@cottsoft.com)
 * @date    2010年11月03日
 * @version v1.0.0
 */
public class ScanPackageHander {
	private static final Logger logger = LoggerFactory.getLogger(ScanPackageHander.class);
    private static boolean excludeInner = true;
    private static boolean checkInOrEx = true;
    private static List<String> classFilters = null;
    
    public static Set<Class<?>> getPackageAllClasses(String basePackage, boolean recursive) {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        String packageName = basePackage;
        if (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.lastIndexOf('.'));
        }
        String package2Path = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(package2Path);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    doScanPackageClassesByFile(classes, packageName, filePath,recursive);
                } else if ("jar".equals(protocol)) {
                    doScanPackageClassesByJar(packageName, url, recursive, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void doScanPackageClassesByJar(String basePackage, URL url, final boolean recursive, Set<Class<?>> classes) {
        String packageName = basePackage;
        String package2Path = packageName.replace('.', '/');
        JarFile jar;
        
        try {
			jar = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
			    JarEntry entry = entries.nextElement();
			    String name = entry.getName();
			    if (!name.startsWith(package2Path) || entry.isDirectory()) {
			        continue;
			    }
			    // 判断是否递归搜索子包
			    if (!recursive
			            && name.lastIndexOf('/') != package2Path.length()) {
			        continue;
			    }
			    // 判断是否过滤 inner class
			    if (excludeInner && name.indexOf('$') != -1) {
			        continue;
			    }
			    String classSimpleName = name.substring(name.lastIndexOf('/') + 1);
			    // 判定是否符合过滤条件
			    if (filterClassName(classSimpleName)) {
			        String className = name.replace('/', '.');
			        className = className.substring(0, className.length() - 6);
			        classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
			    }
			}
		} catch (ClassNotFoundException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
    }

    private static void doScanPackageClassesByFile(Set<Class<?>> classes, String packageName, String packagePath, boolean recursive) {
        try {
			File dir = new File(packagePath);
			if (!dir.exists() || !dir.isDirectory()) {
			    return;
			}
			final boolean fileRecursive = recursive;
			File[] dirfiles = dir.listFiles(new FileFilter() {

			    // 自定义文件过滤规则
			    @Override
			    public boolean accept(File file) {
			        if (file.isDirectory()) {
			            return fileRecursive;
			        }
			        String filename = file.getName();
			        if (excludeInner && filename.indexOf('$') != -1) {
			            return false;
			        }
			        return filterClassName(filename);
			    }
			});
			for (File file : dirfiles) {
			    if (file.isDirectory()) {
			        doScanPackageClassesByFile(classes, packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
			    } else {
			        String className = file.getName().substring(0, file.getName().length() - 6);
			        classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
			    }
			}
		} catch (ClassNotFoundException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
    }

    private static boolean filterClassName(String className) {
        if (!className.endsWith(".class")) {
            return false;
        }
        if (null == classFilters || classFilters.isEmpty()) {
            return true;
        }
        String tmpName = className.substring(0, className.length() - 6);
        boolean flag = false;
        for (String str : classFilters) {
            String tmpreg = "^" + str.replace("*", ".*") + "$";
            Pattern p = Pattern.compile(tmpreg);
            if (p.matcher(tmpName).find()) {
                flag = true;
                break;
            }
        }
        return (checkInOrEx && flag) || (!checkInOrEx && !flag);
    }
}


