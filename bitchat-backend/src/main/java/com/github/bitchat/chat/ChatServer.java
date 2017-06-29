package com.github.bitchat.chat;



import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by leandrolimadasilva on 26/06/17.
 */
@ServerEndpoint(value = "/chat/{room}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatServer {

	private final Logger log = Logger.getLogger(getClass().getName());

	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());


	@OnOpen
	public void open(final Session session, @PathParam("room") final String room) {
		sessions.add(session);
		ChatMessage message = new ChatMessage(
				Json.createObjectBuilder()
				.add("type", "text")
				.add("data", "User has connected")
				.build());

		for(Session s : sessions){
			try {
				s.getBasicRemote().sendObject(message);
			} catch (IOException | EncodeException ex) {
				log.severe(ex.getLocalizedMessage());
			}
		}
		log.info(session.getId() + " has connected");
	}


	@OnMessage
	public void onMessage(ChatMessage message, Session session){
		for(Session s : sessions){
			try {
				s.getBasicRemote().sendObject(message);
			} catch (IOException | EncodeException ex) {
				log.severe(ex.getLocalizedMessage());
			}
		}
		log.info(message.toString());
	}

	/**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
                
        ChatMessage message = new ChatMessage(Json.createObjectBuilder()
                .add("type", "text")
                .add("data", "User has disconnected")
                .build());
        
        for(Session s : sessions){
            try {
                s.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException ex) {
            	log.severe(ex.getLocalizedMessage());
            }
        }
        log.info("User disconnected");
    }    

}
