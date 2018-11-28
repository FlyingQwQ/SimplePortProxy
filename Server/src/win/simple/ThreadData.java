package win.simple;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ThreadData extends Thread{
	
	private Socket inSocket = null;
	private Socket outSocket = null;
	
	public ThreadData(Socket inSocket, Socket outSocket) {
		this.inSocket = inSocket;
		this.outSocket = outSocket;
	}
	
	@Override
	public void run() {
		byte[] bytes = new byte[1024];
		int len = 0;
		for(;;) {
			try {
				if(len == -1) {
					break;
				}
				len = this.inSocket.getInputStream().read(bytes, 0, bytes.length);
				if(len > 0) {
					this.outSocket.getOutputStream().write(bytes, 0, len);
				}
			}catch (SocketException e){
				break;
			}catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(inSocket!=null && !inSocket.isClosed()) {
			try {
				inSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(outSocket!=null && !outSocket.isClosed()) {
			try {
				outSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
