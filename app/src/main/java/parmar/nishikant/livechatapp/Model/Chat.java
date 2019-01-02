package parmar.nishikant.livechatapp.Model;

import java.util.HashMap;

public class Chat {
    public String chat_key;
    public String xuserid;
    public String dpURL;
    public String chatName;
    public String lastmessage;
    public int noofunseenmessages;
    public String lmtime;

    public String getChat_key() {
        return chat_key;
    }

    public void setChat_key(String chat_key) {
        this.chat_key = chat_key;
    }

    public String getXuserid() {
        return xuserid;
    }

    public String getLmtime() {
        return lmtime;
    }

    public void setLmtime(String lmtime) {
        this.lmtime = lmtime;
    }

    public void setXuserid(String xuserid) {
        this.xuserid = xuserid;
    }

    public String getDpURL() {
        return dpURL;
    }

    public void setDpURL(String dpURL) {
        this.dpURL = dpURL;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public int getNoofunseenmessages() {
        return noofunseenmessages;
    }

    public void setNoofunseenmessages(int noofunseenmessages) {
        this.noofunseenmessages = noofunseenmessages;
    }
}
