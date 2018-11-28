package win.simple;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import win.simple.proxy.ProxyProjectServer;

public class ProxyThreadData extends Thread{
	
	/**
	 * 
	 *  接收客户端发送的数据
	 * 
	 */
	
	private Socket proxyclient = null;
	
	public ProxyThreadData(Socket proxyclient) {
		this.proxyclient = proxyclient;
	}
	
	public void run() {
		byte[] bytes = new byte[512];
		int len = 0;
		
		for(;;) {
			try {
				if(len == -1) {
					break;
				}
				len = this.proxyclient.getInputStream().read(bytes, 0, bytes.length);
				if(len > 0) {
					DataAnalysis(bytes, len, this.proxyclient);
				}
			}catch(SocketException e) {
				System.out.println("[" + new Date() + "][*] 代理客户端断开 " + this.proxyclient.getInetAddress().getHostName());
				if(Variable.ClientProxyDataServerArray.get(this.proxyclient) != null) {
					for(ServerSocket serverSocket : Variable.ClientProxyDataServerArray.get(this.proxyclient)) {
						System.out.println("[" + new Date() + "][*] 已经关闭客户 " + this.proxyclient.getInetAddress().getHostName() + " 的隧道服务器" + serverSocket.getLocalPort());
						try {
							serverSocket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}

				if(Variable.ClientProxyServerArray.get(this.proxyclient) != null) {
					for(ServerSocket serverSocket : Variable.ClientProxyServerArray.get(this.proxyclient)) {
						System.out.println("[" + new Date() + "][*] 已经关闭客户 " + this.proxyclient.getInetAddress().getHostName() + " 的项目连接端口" + serverSocket.getLocalPort());
						try {
							serverSocket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				break;
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void DataAnalysis(byte[] bytes, int len, Socket socket) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			String content1 = new String(bytes, 0, len);
			String[] divisionContent1 = content1.split("\r\n");

			StringBuffer stringBuffer = new StringBuffer();

			for(String content2 : divisionContent1) {
				String[] divisionContent2 = content2.split("-");

				if(divisionContent2.length >= 5) {
					if(divisionContent2[0].equals("registerproxy")) {
						new ProxyProjectServer(Integer.parseInt(divisionContent2[1]), Integer.parseInt(divisionContent2[2]), socket).start();
						bufferedWriter.write("tunneldataport-" + divisionContent2[1] + "-" + divisionContent2[2] + "-" + divisionContent2[3] + "-" + divisionContent2[4] + "\r\n");
						bufferedWriter.flush();

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
