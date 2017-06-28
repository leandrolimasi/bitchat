package com.github.bitchat.chat;

import java.util.Date;

/**
 * Created by leandrolimadasilva on 26/06/17.
 */
public class ChatMessage {

    private String content;
    private String sender;
    private Date received;


    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the received
     */
    public Date getReceived() {
        return received;
    }

    /**
     * @param received the received to set
     */
    public void setReceived(Date received) {
        this.received = received;
    }
}
