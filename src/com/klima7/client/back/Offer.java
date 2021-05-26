package com.klima7.client.back;

import java.net.InetAddress;

public class Offer {

	private InetAddress address;
	private int port;
	private String nick;

	public Offer(InetAddress address, int port, String nick) {
		this.address = address;
		this.port = port;
		this.nick = nick;
	}

	@Override
	public String toString() {
		return this.nick + " - " + address.getHostAddress() + ":" + port;
	}
}
