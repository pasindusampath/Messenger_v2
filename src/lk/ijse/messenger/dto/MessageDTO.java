package lk.ijse.messenger.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable {
    private String msg;
    private String to;

    public MessageDTO(String msg, String to) {
        this.msg = msg;
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
