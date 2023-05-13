package lk.ijse.messenger.dto;

import java.util.ArrayList;

public class Message {
    private String msg;
    private ArrayList<String> users;
    private MessageDTO privateMsg;





    public Message(MessageDTO privateMsg) {
        this.privateMsg = privateMsg;
    }

    public Message(String msg){
        this.setMsg(msg);
    }


    public Message(ArrayList<String> users){
        this.users=users;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public MessageDTO getPrivateMsg() {
        return privateMsg;
    }

    public void setPrivateMsg(MessageDTO privateMsg) {
        this.privateMsg = privateMsg;
    }
}
