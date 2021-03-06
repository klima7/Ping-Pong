package com.klima7.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Offer {

	private final InetAddress address;
	private final int port;
	private final String nick;

	public Offer(InetAddress address, int port, String nick) {
		this.address = address;
		this.port = port;
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return this.nick + " - " + address.getHostAddress() + ":" + port;
	}

	public boolean addressEquals(Offer other) {
		return address.equals(other.address) && port == other.port;
	}
}
