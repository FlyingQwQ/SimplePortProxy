package win.simple;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Client {

    Socket ProxyServerSocket = null;

    public static void main(String[] args) {
        System.out.println("[" + new Date() + "] " + Variable.Version);
        System.out.println("[" + new Date() + "] " + "输入help查看帮助");
        System.out.println("[" + new Date() + "] " + "键入Ctrl+C或输入exit来关闭");
        Client cli = new Client();
        cli.ConnectProxyServer();
    }

    public void ConnectProxyServer() {
        Scanner scanner = new Scanner(System.in);
        String proxyaddress = "";
        int proxyport = 0;
        if(Config.GetServerAddress() != null && Config.GetServerPort() != null) {
        	proxyaddress = Config.GetServerAddress();
        	proxyport = Integer.parseInt(Config.GetServerPort());
        	
        	Variable.ProxyServerAddress = proxyaddress;
        	Variable.ProxyServerPort = proxyport;
        }else {
        	 System.out.println("[" + new Date() + "][*] 请输入代理服务器地址");
             proxyaddress = scanner.nextLine();
             System.out.println("[" + new Date() + "][*] 请输入代理服务器端口");
             proxyport = scanner.nextInt();
             
             Variable.ProxyServerAddress = proxyaddress;
             Variable.ProxyServerPort = proxyport;
        }
       
        try {
            this.ProxyServerSocket = new Socket(proxyaddress, proxyport);
            new ClientThreadData(this.ProxyServerSocket).start();

            System.out.println("[" + new Date() + "] 代理服务器连接成功");
            Config.AddTunnleToServer(this.ProxyServerSocket);

            scann();
        }catch(ConnectException e) {
            System.out.println("[" + new Date() + "][*] 无法连接到代理服务器");
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void scann() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                String proxyserverport = "";
                String projectclientport = "";
                String localhost = "";
                String localhostport = "";

                while(true) {
                    String line = scanner.nextLine();

                    try {
                        if(line.equals("newtunnle")) {
                            System.out.println("[" + new Date() + "] 请输入本地域名或IP地址：");
                            localhost = scanner.nextLine();
                            System.out.println("[" + new Date() + "] 请输入本地项目服务器端口：");
                            localhostport = scanner.nextLine();
                            System.out.println("[" + new Date() + "] 请输入隧道服务器连接端口(50000-65536)：");
                            proxyserverport = scanner.nextLine();
                            System.out.println("[" + new Date() + "] 请输入项目客户端连接端口(1000-49999)：");
                            projectclientport = scanner.nextLine();
                            ProxyServerSocket.getOutputStream().write(("registerproxy-"+ proxyserverport + "-" + projectclientport + "-" + localhost + "-" + localhostport).getBytes());
                            ProxyServerSocket.getOutputStream().flush();
                        }

                        if(line.equals("exit")) {
                            System.exit(0);
                        }

                        if(line.equals("help")) {
                            System.out.println(Variable.Help.toString());
                        }
                        
                        if(line.equals("")) {
                        	System.out.print("[" + new Date() + "] >>");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
