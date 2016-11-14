# thrift-framework
================

Thrift JAVA 框架，服务端自动描述注册服务，客户端Client代理。

# Maven dependency
```
<dependency>
  <groupId>com.cottsoft.thrift.framework</groupId>
  <artifactId>thrift-framework</artifactId>
  <version>0.0.1</version>
</dependency>
```

# Download
https://oss.sonatype.org/content/groups/public/com/cottsoft/thrift/framework/thrift-framework/0.0.1/thrift-framework-0.0.1.jar

# Demo
## Server
```
@Service("sysUserServiceImpl")
@ThriftService(service = "com.cottsoft.thrift.framework.rpc.user.SysUserService")
public class SysUserServiceImpl implements Iface {

	@Override
	public int addSysUser(SysUser sysUser) throws ThriftServiceException,
			TException {
		// TODO Auto-generated method stub
		return 0;
	}
}
```
### Server Spring xml
```
	<!-- 服务工厂 -->
	<bean id="serverFactory"
		class="com.cottsoft.thrift.framework.server.ThriftMultiBinaryServerFactory">
		<property name="baseServiceImplPackage" value="com.cottsoft.thrift.framework.server.service" />
		<property name="port" value="${port.default}" />
		<property name="timeout" value="${timeout.default}" />
	</bean>

	<!-- 使用服务工厂启动服务 -->
	<bean factory-bean="serverFactory" factory-method="start" />
```


## Client
```
public class ClientTest {
	public static String rpcPackage = "com.cottsoft.thrift.framework.server";
	public static String serviceModuel = "user";
	public static String serverIp = "127.0.0.1";
	public static int serverPort = 19090;
	public static int timeout = 2000;
	
	public static void main(String[] args) {
		ClientTest test = new ClientTest();
		test.execute();
	}
	
	public void execute(){
		BaseClientFactory clientFactory = null;
		try {
			clientFactory = getFactory("SysUserService");
			SysUserService.Client client = (SysUserService.Client) clientFactory.getClient();
		
			//client.addSysUser(sysUser);
			//client.deleteSysUser(sysUser);
			List<SysUser> userList = client.getSysUserList();
			System.out.println(userList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(clientFactory!=null){
				try {
					clientFactory.destroy();
				} catch (ThriftException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public BaseClientFactory getFactory(String serviceName){
		String service = new StringBuffer().append(rpcPackage).append(".").append(serviceModuel).append(".").append(serviceName).toString();
		BaseClientFactory clientFactory = new ThriftBinaryClientFactory(serverIp, serverPort,timeout, service);
		return clientFactory;
	}

}
```
