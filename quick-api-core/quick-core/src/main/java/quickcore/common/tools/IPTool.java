package quickcore.common.tools;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPTool {
    public static String getLocalIp() {
        String ip = "";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ip = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ip;
    }
}
