package com.github.bitchat.chat;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by leandrolimadasilva on 26/06/17.
 */
public class MessageEncoder implements Encoder.Text<ChatMessage> {


    /**
     * Encode the given object into a String.
     *
     * @param message the object being encoded.
     * @return the encoded object as a string.
     */
    @Override
    public String encode(ChatMessage message) throws EncodeException {
        return Json.createObjectBuilder()
                .add("content", message.getContent())
                .add("sender", message.getSender())
                .add("received", "")
                .build().toString();
    }

    /**
     * This method is called with the endpoint configuration object of the
     * endpoint this encoder is intended for when
     * it is about to be brought into service.
     *
     * @param config the endpoint configuration object when being brought into
     *               service
     */
    @Override
    public void init(EndpointConfig config) {

    }

    /**
     * This method is called when the encoder is about to be removed
     * from service in order that any resources the encoder used may
     * be closed gracefully.
     */
    @Override
    public void destroy() {

    }
}
