package win.simple;

import java.io.IOException;
import java.net.Socket;

public class Heartbeat extends Thread{

    private Socket socket = null;

    public Heartbeat(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        for(;;) {
            try {
                this.socket.getOutputStream().write(1);
                this.socket.getOutputStream().flush();
                Thread.sleep(2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
