package com.klima7.server;

import java.net.Socket;

public class Client {

	private String nick;
	private Socket socket;

	public Client(String nick, Socket socket) {
		this.nick = nick;
		this.socket = socket;
	}

	public String getNick() {
		return nick;
	}

	public Socket getSocket() {
		return socket;
	}
}
