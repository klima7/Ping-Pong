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
		LOGGER.info("Inviting process started");

		sendInviteMessage(socket);
		String nick = receiveNick(socket);

		if(nick.equals(serverNick)) {
			LOGGER.info("Nick " + nick + " is invalid");
			sendNickInvalidMessage(socket);
			return null;
		}

		else {
			LOGGER.info("Nick " + nick + " is valid");
			sendNickValidMessage(socket);
			return nick;
		}
	}

	private void sendInviteMessage(Socket socket) throws IOException {
		sendMessage(socket, "INVITE");
	}

	private void sendNickValidMessage(Socket socket) {
		sendMessage(socket, "NICK VALID");
	}

	private void sendNickInvalidMessage(Socket socket) {
		sendMessage(socket, "NICK INVALID");
	}

	private void sendMessage(Socket socket, String message) {
		LOGGER.debug("Sending message " + message);
		try {
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.writeUTF(message);
			output.flush();
		} catch (IOException e) {
			LOGGER.warn("Exception occurred during sending this message: " + message, e);
		}
	}

	private String receiveNick(Socket socket) throws IOException {
		LOGGER.debug("Waiting to receive nick");
		DataInputStream input = new DataInputStream(socket.getInputStream());
		String nick = input.readUTF();
		LOGGER.debug("Nick " + nick + " received");
		return nick;
	}
}
