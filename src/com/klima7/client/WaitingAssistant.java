package com.klima7.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WaitingAssistant extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(WaitingAssistant.class);

	private final Socket socket;
	private final String nick;
	private final PositionNotifierListener listener;

	public WaitingAssistant(Socket socket, String nick, PositionNotifierListener listener) {
		this.socket = socket;
		this.nick = nick;
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
			while(!interrupted()) {
				String message = receiveMessage();
				LOGGER.debug("Message received: " + message);
				interpretMessage(message);
			}
		} catch (IOException e) {
			LOGGER.warn("Exception during receiving occurred", e);
			listener.onError();
		} catch (InterruptedException ignored) {}
	}

	private String receiveMessage() throws IOException, InterruptedException {
		DataInputStream input = new DataInputStream(socket.getInputStream());
		socket.setSoTimeout(100);

		while(true) {
			try {
				String message = input.readUTF();
				LOGGER.info("Message received: " + message);
				return message;
			} catch (SocketTimeoutException e) {
				if(isInterrupted()) {
					throw new InterruptedException();
				}
			}
		}
	}

	private void interpretMessage(String message) {
		if(message.equals("NICK INVALID")) {
			listener.onInvalidNick();
			interrupt();
		}
		else if(message.equals("NICK VALID")) {
			listener.onValidNick();
			interrupt();
		}
		else if(message.equals("INVITE")) {
			try {
				sendNick();
			} catch (IOException e) {
				LOGGER.warn("Exception during sending nick occurred", e);
				listener.onError();
			}
		}
		else if(message.startsWith("POSITION ")) {
			String positionPart = message.substring(9);
			try {
				int position = Integer.parseInt(positionPart);
				listener.onPositionInQueueChanged(position);
			} catch (NumberFormatException e) {
				LOGGER.warn("Received position in queue is invalid: " + positionPart, e);
				listener.onError();
			}
		}
	}

	private void sendNick() throws IOException {
		LOGGER.debug("Sending nick: " + nick);
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		output.writeUTF(nick);
		output.flush();
	}

	public void quit() {
		interrupt();
	}

	public interface PositionNotifierListener {
		void onPositionInQueueChanged(int position);
		void onInvalidNick();
		void onValidNick();
		void onError();
	}
}
