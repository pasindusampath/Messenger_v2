package lk.ijse.messenger.thread;

import com.sun.xml.internal.ws.api.message.Packet;
import javafx.concurrent.Task;
import lk.ijse.messenger.dto.Message;
import lk.ijse.messenger.dto.MessageDTO;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Handler extends Task<Message> {
    private Socket clientSocket;
    public Handler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    protected Message call() throws Exception {
        BufferedReader inFromServer;
        final InputStream inputStream = clientSocket.getInputStream();
        /*Thread t1 = new Thread(){
            public void run(){
                while (true){
                    try {
                        ObjectInputStream inFromServerObject = new ObjectInputStream(inputStream);
                        System.out.println("Getting Objects");
                        Object o = inFromServerObject.readObject();
                        ArrayList<String> list = (ArrayList<String>)o;
                        for (String s:list){
                            System.out.println(s);
                        }
                        updateValue(new Message(list));
                    } catch (IOException e) {
                        System.out.println(e);
                    } catch (ClassNotFoundException e) {
                        System.out.println(e);
                    }
                }
            }
        };
*/
        while (true) {
            System.out.println("Getting Objects");
            ObjectInputStream inFromServerObject = new ObjectInputStream(inputStream);
            System.out.println("Recived");
            Object o = inFromServerObject.readObject();
            try {
                ArrayList<String> users = (ArrayList<String>)o;
                updateValue(new Message(users));
                continue;
            }catch (Exception e){
                System.out.println(e);
            }
            try {
                MessageDTO ob = (MessageDTO)o;
                updateValue(new Message(ob));
                continue;
            }catch (Exception e){
                System.out.println(e);
            }

            updateValue(new Message(o.toString()));
            if(o.equals("Success ")){
                break;
            }

        }

        return new Message("");
    }

    public void sendMessage(MessageDTO message) throws IOException {
        System.out.println("Sending to server");
        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        outToServer.writeObject(message);
        System.out.println("Sent To Server");
    }
    public Socket getClientSocket(){
        return clientSocket;
    }
}
