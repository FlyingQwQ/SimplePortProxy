package win.simple;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {

	public static ServerSocket proxyServer = null;
	public static List<Socket> proxyClientSocket = new ArrayList<>();

	public void StartProxy(int port) {
		try {
			proxyServer = new ServerSocket(port);
			System.out.println("[" + new Date() + "] 代理服务器创建成功，连接端口:" + port);
			while(proxyServer != null) {
				Socket socket = proxyServer.accept();
				new ProxyThreadData(socket).start();
				System.out.println("[" + new Date() + "] 代理客户端登陆服务器 " + socket.getInetAddress().getHostName());
			}
			
		}catch(BindException e) {
			System.out.println("[" + new Date() + "][*] 创建失败，代理服务器端口:" + port + " 可能被系统防火墙拦截或端口已经被占用！");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("[" + new Date() + "] " + Variable.Version);
		Server server = new Server();
		server.StartProxy(12758);
	}
	
	

}
