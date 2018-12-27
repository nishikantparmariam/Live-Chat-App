package parmar.nishikant.livechatapp.Model;

import java.util.HashMap;

public class Message {
    private String sender;
    private  String message;
    private HashMap<String, Object> isseen;
    private String key;
    private String time;
    private String date;

    public Message(String sender,String message,HashMap<String, Object> isseen,String key,String time,String date) {
        this.sender = sender;
        this.message = message;
        this.isseen = isseen;
        this.key= key;
        this.time= time;
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Message(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HashMap<String, Object> getIsseen() {
        return isseen;
    }

    public void setIsseen(HashMap<String, Object> isseen) {
        this.isseen = isseen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
