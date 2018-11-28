package win.simple;

import net.sf.json.JSONObject;

import java.io.*;

public class Config {

    public static int GetProxyPort() {
        String ConfigJSON = GetJSONConfig();
        if(ConfigJSON != null) {
            return JSONObject.fromObject(ConfigJSON).getInt("proxyport");
        }
        return -1;
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
