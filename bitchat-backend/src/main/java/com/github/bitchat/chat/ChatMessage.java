package com.github.bitchat.chat;

import java.io.StringWriter;

import javax.json.JsonObject;

import javax.json.Json;;

/**
 * Created by leandrolimadasilva on 26/06/17.
 */
public class ChatMessage {

	private JsonObject json;

	/**
	 *  Constructor
	 *  
	 * @param json
	 */
	public ChatMessage(JsonObject json) {
		this.json = json;
	}
	
	/** get json
	 * 
	 * @return
	 */
	public JsonObject getJson() {
		return json;
	}
	
	/** set json
	 * 
	 * @param json
	 */
	public void setJson(JsonObject json) {
		this.json = json;
	}

	@Override
	public String toString(){
		StringWriter writer = new StringWriter();
		Json.createWriter(writer).write(json);
		return writer.toString();
	}
}
