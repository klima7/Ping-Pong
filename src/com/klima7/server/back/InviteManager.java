package com.klima7.server.back;

import com.klima7.app.back.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class InviteManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(InviteManager.class);

	private final InviteManagerListener listener;
	private final String serverNick;

	public InviteManager(InviteManagerListener listener, String serverNick) {
		this.listener = listener;
		this.serverNick = serverNick;
	}

	public void invite(Socket socket) {
		CompletableFuture.runAsync(() -> inviteBlocking(socket));
	}

	private void inviteBlocking(Socket socket) {
		LOGGER.info("Inviting process started");

		try {
			sendInviteMessage(socket);
			String nick = receiveNick(socket);

			if(nick.equals(serverNick)) {
				LOGGER.info("Nick " + nick + " is invalid");
				sendNickInvalidMessage(socket);
				listener.onNickInvalid(socket);
			}
			else {
				LOGGER.info("Nick " + nick + " is valid");
				sendNickValidMessage(socket);
				listener.onNickValid(new Client(nick, socket));
			}
		} catch (IOException e) {
			LOGGER.warn("Exception during invite process occurred", e);
			listener.onInviteError(socket);
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

	private void sendMessage(Socket socket, String text) {
		LOGGER.debug("Sending message " + text);
		try {
			OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
			String message = text + Constants.COMMAND_END;
			output.write(message);
			output.flush();
		} catch (IOException e) {
			LOGGER.warn("Exception occurred during sending this message: " + text, e);
		}
	}

	private String receiveNick(Socket socket) throws IOException {
		LOGGER.debug("Waiting to receive nick");
		InputStream input = socket.getInputStream();
		byte[] bytes = new byte[1024];
		int bytesRead = input.read(bytes);
		String nick = new String(bytes, 0, bytesRead);
		LOGGER.debug("Nick " + nick + " received");
		return nick;
	}

	public interface InviteManagerListener {
		void onNickInvalid(Socket socket);
		void onNickValid(Client client);
		void onInviteError(Socket socket);
	}
}
