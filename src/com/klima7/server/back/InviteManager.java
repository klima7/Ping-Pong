package com.klima7.server.back;

import com.klima7.app.back.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class InviteManager {

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
		try {
			sendInviteMessage(socket);
			String nick = receiveNick(socket);

			if(nick.equals(serverNick)) {
				sendNickInvalidMessage(socket);
				listener.onNickInvalid(socket);
			}
			else {
				sendNickValidMessage(socket);
				listener.onNickValid(new Client(nick, socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
			listener.onInviteError(socket);
		}
	}

	private void sendInviteMessage(Socket socket) throws IOException {
		sendMessage(socket, "INVITE");
	}

	private void sendNickValidMessage(Socket socket) {
		System.out.println("InviteManager.sendNickValidMessage");
		sendMessage(socket, "NICK VALID");
	}

	private void sendNickInvalidMessage(Socket socket) {
		sendMessage(socket, "NICK INVALID");
	}

	private void sendMessage(Socket socket, String text) {
		try {
			OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
			String message = text + Constants.COMMAND_END;
			System.out.println("Sending " + message);
			output.write(message);
			output.flush();
		} catch (IOException e) {
			// Można potem usunąć
			e.printStackTrace();
		}
	}

	private String receiveNick(Socket socket) throws IOException {
		InputStream input = socket.getInputStream();
		byte[] bytes = new byte[1024];
		int bytesRead = input.read(bytes);
		return new String(bytes, 0, bytesRead);
	}

	public interface InviteManagerListener {
		void onNickInvalid(Socket socket);
		void onNickValid(Client client);
		void onInviteError(Socket socket);
	}
}
