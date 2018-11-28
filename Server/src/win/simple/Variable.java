package win.simple;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Variable {
    public static String Version = "版本: 1.0.0";

    public static Map<Socket, List<ServerSocket>> ClientProxyDataServerArray = new HashMap<>();
    public static Map<Socket, List<ServerSocket>> ClientProxyServerArray = new HashMap<>();
}
