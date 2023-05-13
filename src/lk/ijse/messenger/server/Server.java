package lk.ijse.messenger.server;


import lk.ijse.messenger.dto.MessageDTO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket host = new ServerSocket(9001);
        System.out.println("Server Running");
        HashMap<String, Socket> clients = new HashMap<>();

        while (true) {

            Socket newClient = host.accept();
            Thread t1 = new Thread() {
                public void run() {
                    BufferedReader inFromClient;

                    ObjectOutputStream outToClient;
                    String name="";
                    try {
                        while (!newClient.isClosed()) {
                            inFromClient = new BufferedReader(new InputStreamReader(newClient.getInputStream()));
                            name = inFromClient.readLine();
                            if (clients.get(name) == null) {
                                clients.put(name, newClient);
                                outToClient = new ObjectOutputStream(newClient.getOutputStream());
                                outToClient.writeObject("Success" + ' ');
                                sendAllClientsInTheServer(clients,name);
                                System.out.println(name+" Joined To Server");
                                break;
                            }
                            outToClient = new ObjectOutputStream(newClient.getOutputStream());
                            outToClient.writeObject("Duplicate Name " + name + ' ');
                        }
                        while (true) {
                            System.out.println("Waiting for messages");
                            ObjectInputStream ob = new ObjectInputStream(newClient.getInputStream());
                            System.out.println("object catching");
                            Object in = ob.readObject();
                            System.out.println("object caught");
                            MessageDTO msg = (MessageDTO)in;
                            sendAll(msg, clients,newClient,name);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        clients.remove(name);
                    }
                }
            };
            t1.start();
        }
    }

    public static void sendAll(MessageDTO msg, HashMap<String, Socket> clients, Socket client, String name) throws IOException {
        Set<String> strings = clients.keySet();
        if(msg.getTo().equals("All")){
            for (String key:strings) {
                Socket socket = clients.get(key);
                if(socket.equals(client)){
                    continue;
                }
                if (!socket.isClosed()) {
                    ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
                    String out = name+" : "+msg.getMsg()+' ';
                    System.out.println();
                    outToClient.writeObject(out);
                }
            }
            return;
        }
        Socket socket = clients.get(msg.getTo());
        if(socket!=null){
            if (!socket.isClosed()) {
                ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
                String out = name+" : "+msg.getMsg()+' ';
                System.out.println();
                outToClient.writeObject(new MessageDTO(out,msg.getTo()));
            }
        }

    }

    public static void sendAllClientsInTheServer(HashMap<String,Socket> clients,String cli){
        ArrayList<String> users = new ArrayList<String>();
        users.add("All");
        Set<String> strings = clients.keySet();
        for(String name:strings){
            users.add(name);
        }

        Collection<Socket> values = clients.values();
        for(Socket socket:values){
            try {
                ObjectOutputStream ob = new ObjectOutputStream(socket.getOutputStream());
                ob.writeObject(users);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}