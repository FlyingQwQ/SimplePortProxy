package win.simple;

import java.util.HashMap;
import java.util.Map;

public class Variable {
    public final static String Version = "版本: 1.0.0";
    public final static StringBuffer Help = new StringBuffer();
    
    public static String ProxyServerAddress = null;
    public static int ProxyServerPort = 0;

    static {
        Help.append("——————————————————————\r\n");
        Help.append("newtunnle\t\t创建新的隧道\r\n");
        Help.append("exit\t\t\t关闭软件\r\n");
        Help.append("port 端口号\t\t查看端口是否被占用(建议测试一下在创建)\r\n");
        Help.append("——————————————————————\r\n");
    }
}
