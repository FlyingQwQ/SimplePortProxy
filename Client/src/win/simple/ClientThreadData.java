package win.simple;

import win.simple.proxy.ConnectTunnleServer;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class ClientThreadData extends Thread{

    private Socket ProxyServerSocket = null;

    public ClientThreadData(Socket ProxyServerSocket) {
        this.ProxyServerSocket = ProxyServerSocket;
    }

    @Override
    public void run() {
        byte[] bytes = new byte[512];
        int len = 0;
        for(;;) {
            try {
                if(len == -1) {
                    break;
                }
                len = this.ProxyServerSocket.getInputStream().read(bytes, 0, bytes.length);
                if(len > 0) {
                    DataAnalysis(bytes, len);
                }
            }catch(SocketException e) {
                System.out.println("[" + new Date() + "] 代理服务器断开");
                System.exit(0);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void DataAnalysis(byte[] bytes, int len) {
        String content1 = new String(bytes, 0, len);
        String[] divisionContent1 = content1.split("\r\n");

        for(String conten2 : divisionContent1) {
            String[] divisionContent2 = conten2.split("-");

            if(divisionContent2.length >= 5) {
                if(divisionContent2[0].equals("tunneldataport")) {
                    new ConnectTunnleServer(Integer.parseInt(divisionContent2[1]), Integer.parseInt(divisionContent2[2]), divisionContent2[3], Integer.parseInt(divisionContent2[4])).start();
                }
            }

            if(divisionContent2.length >= 4) {
                if(divisionContent2[0].equals("info")) {
                    if(divisionContent2[1].equals("createtunnlesuccess")) {
                        System.out.println("[" + new Date() + "] 隧道创建成功，外网项目客户端连接地址:" + Variable.ProxyServerAddress + ":" + divisionContent2[3]);
                    }
                }

                if(divisionContent2[0].equals("error")) {
                    if(divisionContent2[1].equals("createtunnlefail")) {
                        System.out.println("[" + new Date() + "][*] 隧道创建失败，隧道通讯端口" + divisionContent2[2] + " 外网项目客户端连接端口:" + divisionContent2[3].replace("tunneldataport", "") + "，端口可能已经被占用！");
                    }
                }
            }
        }

    }
}
