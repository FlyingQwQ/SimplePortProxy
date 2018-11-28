# SimplePortProxy
<br />
<br />

## 增强配置
    {
	"serveraddress":"127.0.0.1",        #代理服务器地址
	"serverport":"12758",               #代理服务器端口
	
    "tunnle":[                          #隧道（可以创建多个）
		{"registerproxy":[
			{
			"localhost":"127.0.0.1",    #本地项目服务器地址
			"localhostport":"25565",    #本地项目服务器端口
			"teledataport":"4561",      #隧道传输端口
			"teleport":"1275"           #外网客户端连接端口
			}
		]},
		
		{"registerproxy":[
			{
			"localhost":"127.0.0.2",
			"localhostport":"25565",
			"teledataport":"4562",
			"teleport":"1276"
			}
		]}
	]
    }


## 进度
- [ ] 服务端配置自定义端口
- [ ] 禁用指定地址登陆服务端
- [ ] 自定义端口吞吐量
- [ ] 断开重连
