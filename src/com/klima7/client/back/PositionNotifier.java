package com.klima7.client.back;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class PositionNotifier extends Thread {

	private boolean running;
	private boolean invited;

	private final Socket socket;
	private final String nick;
	private final PositionNotifierListener listener;

	public PositionNotifier(Socket socket, String nick, PositionNotifierListener listener) {
		this.socket = socket;
		this.nick = nick;
		this.listener = listener;
	}

	@Override
	public void run() {
		running = true;
		try {
			while(running) {
				InputStream input = socket.getInputStream();
				byte[] bytes = new byte[1024];
				int bytesRead = input.read(bytes);
				String message = new String(bytes, 0, bytesRead);
				interpretMessage(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
			listener.onError();
		}
	}

	private void interpretMessage(String message) {
		if(message.equals("NICK INVALID"))
			listener.onInvalidNick();
		else if(message.equals("VALID NICK"))
			listener.onValidNick();
		else if(message.equals("INVITE")) {
			try {
				sendNick();
			} catch (IOException e) {
				e.printStackTrace();
				listener.onError();
			}
		}
		else if(message.startsWith("POSITION ")) {
			String positionPart = message.substring(9);
			try {
				int position = Integer.parseInt(positionPart);
				listener.onPositionChanged(position);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				listener.onError();
			}
		}
	}

	private void sendNick() throws IOException {
		OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
		output.write(nick);
		output.flush();
	}

	public void stopNotifying() {
		running = false;
	}

	public interface PositionNotifierListener {
		void onPositionChanged(int position);
		void onInvalidNick();
		void onValidNick();
		void onError();
	}
}
