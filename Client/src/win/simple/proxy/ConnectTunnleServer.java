package win.simple.proxy;

import win.simple.ThreadData;
import win.simple.Variable;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;

public class ConnectTunnleServer extends Thread{

    private Socket TunnleServerSocket = null;       //连接到隧道服务器
    private Socket ProjectServerSocket = null;      //连接到项目服务器
    private int port = 0;       //隧道服务器端口
    private int tunnleport = 0; //项目客户端连接端口
    private String localhost = "";  //本地项目地址
    private int localhostport = 0;  //本地项目端口

    public ConnectTunnleServer(int port, int tunnleport, String localhost, int localhostport) {
        this.port = port;
        this.tunnleport = tunnleport;
        this.localhost = localhost;
        this.localhostport = localhostport;
    }

    @Override
    public void run() {
        try {
            System.out.println("[" + new Date() + "] 隧道创建成功，外网项目客户端连接端口:" + this.tunnleport + " --> " + this.localhost + ":" + this.localhostport);
            while(true) {
                this.TunnleServerSocket = new Socket(Variable.ProxyServerAddress, this.port);
                InputStream in = this.TunnleServerSocket.getInputStream();
                while(in.available() < 1) {

                }
                this.ProjectServerSocket = new Socket(this.localhost, this.localhostport);

                new ThreadData(this.TunnleServerSocket, this.ProjectServerSocket).start();
                new ThreadData(this.ProjectServerSocket, this.TunnleServerSocket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
