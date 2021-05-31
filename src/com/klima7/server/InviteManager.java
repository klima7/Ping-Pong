package com.klima7.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class InviteManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(InviteManager.class);

	private final String serverNick;

	public InviteManager(String serverNick) {
		this.serverNick = serverNick;
	}

	public String invite(Socket socket) throws IOException {
		LOGGER.info("Inviting started");

		sendMessage(socket, "INVITE");
		String nick = receiveNick(socket);

		if(nick.equals(serverNick)) {
			LOGGER.info("Nick " + nick + " is invalid");
			sendMessage(socket, "NICK INVALID");
			return null;
		}

		else {
			LOGGER.info("Nick " + nick + " is valid");
			sendMessage(socket, "NICK VALID");
			return nick;
		}
	}

	private void sendMessage(Socket socket, String message) throws IOException {
		LOGGER.debug("Sending message " + message);
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		output.writeUTF(message);
		output.flush();
	}

	private String receiveNick(Socket socket) throws IOException {
		LOGGER.debug("Waiting to receive nick");
		DataInputStream input = new DataInputStream(socket.getInputStream());
		String nick = input.readUTF();
		LOGGER.debug("Nick " + nick + " received");
		return nick;
	}
}
