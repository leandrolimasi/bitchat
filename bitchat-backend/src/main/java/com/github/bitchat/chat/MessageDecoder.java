package com.github.bitchat.chat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.util.Date;

/**
 * Created by leandrolimadasilva on 26/06/17.
 */
public class MessageDecoder implements Decoder.Text<ChatMessage>  {
    /**
     * Decode the given String into an object of type T.
     *
     * @param textMessage string to be decoded.
     * @return the decoded message as an object of type T
     */
    @Override
    public ChatMessage decode(String textMessage) throws DecodeException {
        ChatMessage message = new ChatMessage();
        JsonObject jsonObject = Json.createReader(new StringReader(textMessage)).readObject();
        message.setContent(jsonObject.getString("content"));
        message.setSender(jsonObject.getString("sender"));
        message.setReceived(new Date());
        return message;
    }

    /**
     * Answer whether the given String can be decoded into an object of type T.
     *
     * @param s the string being tested for decodability.
     * @return whether this decoder can decoded the supplied string.
     */
    @Override
    public boolean willDecode(String s) {
        return true;
    }

    /**
     * This method is called with the endpoint configuration object of the
     * endpoint this decoder is intended for when
     * it is about to be brought into service.
     *
     * @param config the endpoint configuration object when being brought into
     *               service
     */
    @Override
    public void init(EndpointConfig config) {

    }

    /**
     * This method is called when the decoder is about to be removed
     * from service in order that any resources the encoder used may
     * be closed gracefully.
     */
    @Override
    public void destroy() {

    }
}
