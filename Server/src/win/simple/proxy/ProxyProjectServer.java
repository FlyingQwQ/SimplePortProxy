package win.simple.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import win.simple.ThreadData;
import win.simple.Variable;

public class ProxyProjectServer extends Thread{

	/**
	 * 
	 *  创建隧道以及项目连接端口
	 * 
	 */
	
	private ServerSocket teleProjectDataServer = null;	//代理客户端传输数据服务器
	private ServerSocket teleProjectServer = null;		//项目客户端连接服务器
	
	public ProxyProjectServer(int teledataport, int teleport, Socket socket) {
		try {
			teleProjectServer = new ServerSocket(teleport);
			teleProjectDataServer = new ServerSocket(teledataport);

			if(Variable.ClientProxyDataServerArray.get(socket) == null) {
				List<ServerSocket> userproxyVariable = new ArrayList<>();
				userproxyVariable.add(teleProjectDataServer);
				Variable.ClientProxyDataServerArray.put(socket, userproxyVariable);
			}else {
				List<ServerSocket> userproxyVariable = Variable.ClientProxyDataServerArray.get(socket);
				userproxyVariable.add(teleProjectDataServer);
				Variable.ClientProxyDataServerArray.put(socket, userproxyVariable);
			}

			if(Variable.ClientProxyServerArray.get(socket) == null) {
				List<ServerSocket> userproxyVariable = new ArrayList<>();
				userproxyVariable.add(teleProjectServer);
				Variable.ClientProxyServerArray.put(socket, userproxyVariable);
			}else {
				List<ServerSocket> userproxyVariable = Variable.ClientProxyServerArray.get(socket);
				userproxyVariable.add(teleProjectServer);
				Variable.ClientProxyServerArray.put(socket, userproxyVariable);
			}

			socket.getOutputStream().write(("info-createtunnlesuccess-" + teledataport + "-" + teleport).getBytes());
			socket.getOutputStream().flush();
			System.out.println("[" + new Date() + "] 创建隧道成功，隧道服务器端口:" + teledataport + "，项目连接端口:" + teleport);
		} catch (IOException e) {
			try {
				socket.getOutputStream().write(("error-createtunnlefail-" + teledataport + "-" + teleport).getBytes());
				socket.getOutputStream().flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("[" + new Date() + "][*] 创建隧道失败，隧道服务器端口:" + teledataport + "，项目连接端口:" + teleport + " 可能被系统防火墙拦截或端口已经被占用！");
		}
	}
	
	@Override
	public void run() {
		try {
			for(;;) {
				Socket teleProjectData = this.teleProjectDataServer.accept();
				Socket teleProject = this.teleProjectServer.accept();

				new ThreadData(teleProject, teleProjectData).start();
				new ThreadData(teleProjectData, teleProject).start();

				System.out.println("[" + new Date() + "] 项目客户端" + teleProject.getInetAddress().getHostAddress() + "请求连接");
			}
		}catch(NullPointerException e) {

		}catch(SocketException e) {

		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
