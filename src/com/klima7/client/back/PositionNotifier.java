package com.klima7.client.back;

import com.klima7.app.back.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class PositionNotifier extends Thread {

	private boolean running;

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
				String message = receiveMessage();
				System.out.println("Received: " + message);
				interpretMessage(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
			listener.onError();
		}
	}

	private String receiveMessage() throws IOException {
		String message = "";
		InputStream input = socket.getInputStream();

		char c;
		do {
			c = (char)input.read();
			message += c;
		} while(c != Constants.COMMAND_END);

		return message.substring(0, message.length()-1);
	}

	private void interpretMessage(String message) {
		if(message.equals("NICK INVALID"))
			listener.onInvalidNick();
		else if(message.equals("NICK VALID"))
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
