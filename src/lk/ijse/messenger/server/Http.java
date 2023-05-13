package lk.ijse.messenger.server;



import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class Http {
    public static void main(String[] args) {
        while (true){
            try {
                String hostName = InetAddress.getLocalHost().getHostAddress();
                String replace = hostName.replace(".", " ");
                String[] split = replace.split(" ");
                System.out.println(hostName);
                System.out.println(Arrays.toString(split));
                String temp=split[0]+"."+split[1]+"."+split[2]+".";

                for(int i=0;i<65535;i++) {
                    try {
                        Socket clientSocket = new Socket();
                        InetSocketAddress isa=new InetSocketAddress("192.168.8.105", i);
                        clientSocket.connect(isa,1);
                        //new Alert(Alert.AlertType.INFORMATION, "Connect With "+isa.getHostName()).showAndWait();
                        System.out.println("Connected With "+isa.getHostName()+i);
                        clientSocket.close();
                    } catch (Exception e) {
                        //System.out.println("Failed : "+ i);
                    }
                }
                //new Alert(Alert.AlertType.ERROR, "Connect With Server Error").showAndWait();
                return;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
