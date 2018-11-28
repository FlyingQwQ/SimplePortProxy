package win.simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Config {
	
	public static String GetServerAddress() {
		String ConfigJSON = GetJSONConfig();
		return JSONObject.fromObject(ConfigJSON).getString("serveraddress");
	}
	
	public static String GetServerPort() {
		String ConfigJSON = GetJSONConfig();
		return JSONObject.fromObject(ConfigJSON).getString("serverport");
	}
	
	public static JSONArray GetTunnleList() {
		String ConfigJSON = GetJSONConfig();
		return JSONArray.fromObject(JSONObject.fromObject(ConfigJSON).getJSONArray("tunnle"));
	}
	
	public static void AddTunnleToServer(Socket socket) {
		for(int i = 0; i < JSONArray.fromObject(GetTunnleList()).size(); i++) {
			JSONArray jarray = JSONArray.fromObject(JSONObject.fromObject(JSONArray.fromObject(GetTunnleList()).get(i)).getString("registerproxy"));
			String localhost = JSONObject.fromObject(jarray.getString(0)).getString("localhost");
			String localhostport = JSONObject.fromObject(jarray.getString(0)).getString("localhostport");
			String teledataport = JSONObject.fromObject(jarray.getString(0)).getString("teledataport");
			String teleport = JSONObject.fromObject(jarray.getString(0)).getString("teleport");
			try {
				socket.getOutputStream().write(("registerproxy-"+ teledataport + "-" + teleport + "-" + localhost + "-" + localhostport + "\r\n").getBytes());
				socket.getOutputStream().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String GetJSONConfig() {
		File file = new File(".\\Config.json");
		try {
			FileInputStream fileis = new FileInputStream(file);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileis));
			String line = "";
			StringBuffer sb = new StringBuffer();
			while((line = bufferedReader.readLine()) != null) {
				sb.append(line + "\r\n");
			}
			bufferedReader.close();
			return sb.toString();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
