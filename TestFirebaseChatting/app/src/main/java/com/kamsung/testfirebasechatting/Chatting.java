package com.kamsung.testfirebasechatting;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kkb on 2017-02-24.
 */

public class Chatting {
    private String user;
    private String content;
    private String date;

    public Chatting(){}

    public Chatting(String user, String content) {
        this.user = user;
        this.content = content;

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.date = df.format(date);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = date;
    }
}
