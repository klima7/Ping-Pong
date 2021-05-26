package com.klima7.client.back;

import com.klima7.app.back.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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
		LOGGER.info("Starting WaitingAssistant");

		try {
			while(!interrupted()) {
				String message = receiveMessage();
				LOGGER.debug("Message received: " + message);
				interpretMessage(message);
			}
		} catch (IOException e) {
			LOGGER.warn("Exception during receiving occurred", e);
			listener.onError();
		} catch (InterruptedException ignored) { }

		LOGGER.info("Stopping WaitingAssistant");
	}

	private String receiveMessage() throws IOException, InterruptedException {
		String message = "";
		InputStream input = socket.getInputStream();
		socket.setSoTimeout(100);

		char c = 0;
		do {
			try {
				c = (char)input.read();
				message += c;
			} catch (SocketTimeoutException ignored) {}

			if(interrupted())
				throw new InterruptedException();

		} while(c != Constants.COMMAND_END);

		return message.substring(0, message.length()-1);
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
		OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
		output.write(nick);
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
