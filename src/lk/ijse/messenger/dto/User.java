package lk.ijse.messenger.dto;

import lk.ijse.messenger.thread.Handler;

import java.net.Socket;

public class User {
    private Handler handler;
    private String name;

    public User(Handler handler, String name) {
        this.handler = handler;
        this.name = name;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
