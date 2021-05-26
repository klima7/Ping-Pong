package com.klima7.server.back;

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
			sendInvite(socket);
			String nick = receiveNick(socket);
			if(nick.equals(serverNick))
				listener.onNickInvalid(socket);
			else
				listener.onNickValid(new Client(nick, socket));
		} catch (IOException e) {
			e.printStackTrace();
			listener.onInviteError(socket);
		}
	}

	private void sendInvite(Socket socket) throws IOException {
		OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
		output.write("INVITE");
		output.flush();
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
