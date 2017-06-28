package com.github.bitchat.chat;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import java.text.SimpleDateFormat;

/**
 * Created by leandrolimadasilva on 26/06/17.
 */
@ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatClient {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @OnMessage
    public void onMessage(ChatMessage message) {
        System.out.println(String.format("[%s:%s] %s",
                simpleDateFormat.format(message.getReceived()), message.getSender(), message.getContent()));
    }
}
