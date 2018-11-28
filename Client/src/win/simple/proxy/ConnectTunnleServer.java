package win.simple.proxy;

import win.simple.ThreadData;
import win.simple.Variable;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
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
            while(true) {
                this.TunnleServerSocket = new Socket(Variable.ProxyServerAddress, this.port);
                InputStream in = this.TunnleServerSocket.getInputStream();
                while(in.available() < 1) {

                }
                this.ProjectServerSocket = new Socket(this.localhost, this.localhostport);

                new ThreadData(this.TunnleServerSocket, this.ProjectServerSocket).start();
                new ThreadData(this.ProjectServerSocket, this.TunnleServerSocket).start();
            }

        }catch(ConnectException e) {
            System.out.println("[" + new Date() + "][*] 无法连接到隧道通讯服务器，隧道连接端口" + this.port + "有误！");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
