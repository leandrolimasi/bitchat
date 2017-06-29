package com.github.bitchat.chat;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by leandrolimadasilva on 26/06/17.
 */
public class MessageEncoder implements Encoder.Text<ChatMessage> {

	@Override
    public String encode(ChatMessage message) throws EncodeException {
        return message.getJson().toString();
    }

    @Override
    public void init(EndpointConfig config) {
        System.out.println("Init");
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
