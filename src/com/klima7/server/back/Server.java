package com.klima7.server.back;

import java.io.IOException;

public class Server {

	private static Server instance;

	private String nick;
	private TcpManager tcpManager;
	private UdpManager udpManager;

	public static Server getInstance() {
		if(instance == null)
			instance = new Server();
		return instance;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void start() throws IOException {
		tcpManager = new TcpManager();
		udpManager = new UdpManager(tcpManager.getPort(), nick);
		udpManager.start();
	}

	public void stopDiscoveryListen() {
		udpManager.stopListening();
	}
}
