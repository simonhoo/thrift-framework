namespace java com.cottsoft.thrift.framework.rpc.user
include "Common.thrift"

/* 系统用户
 * 表：SYS_USER
 */
struct SysUser {
        1: i32 pid;
        2: string userName;
        3: string memberId; 
        4: string department;
        5: string jobTitle;
        6: string mail;
        7: string pwd;
        8: string photoUrl;  
}

service SysUserService {
	i32 addSysUser(1:SysUser sysUser) throws (1:Common.ThriftServiceException e);
	i32 deleteSysUser(1:SysUser sysUser) throws (1:Common.ThriftServiceException e);
	i32 updateSysUser(1:SysUser sysUser) throws (1:Common.ThriftServiceException e);
	SysUser querySysUserByName(1:string userName);
	list<SysUser> getSysUserList();
	list<SysUser> getSysUserByParameter(1:map<string,string> parameter);
}

