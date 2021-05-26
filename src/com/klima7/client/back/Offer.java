package com.klima7.client.back;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Offer {

	private final InetAddress address;
	private final int port;
	private final String nick;
	private Socket socket;

	public Offer(InetAddress address, int port, String nick) {
		this.address = address;
		this.port = port;
		this.nick = nick;
	}

	public void connect() throws IOException {
		socket = new Socket(address, port);
	}

	public Socket getSocket() {
		return socket;
	}

	@Override
	public String toString() {
		return this.nick + " - " + address.getHostAddress() + ":" + port;
	}
}
