package com.klima7.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public class Server {

	public static final int MIN_PORT_NUMBER = 1;
	public static final int MAX_PORT_NUMBER = 65_535;

	private static Server instance;

	private String nick;
	private final ServerSocket serverSocket;
	private DiscoveryListener listener;

	public static Server getInstance() {
		if(instance == null)
			instance = new Server();
		return instance;
	}

	private Server() {
		this.serverSocket = randomSocket();
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void startDiscoveryListen() throws IOException {
		if(listener != null)
			throw new IOException("Already listening");
		listener = new DiscoveryListener(serverSocket.getLocalPort(), nick);
		listener.start();
	}

	public void stopDiscoveryListen() {
		listener.stopListening();
		listener = null;
	}

	private ServerSocket randomSocket() {
		while(true) {
			Random random = new Random();
			int port = random.nextInt(MAX_PORT_NUMBER - MIN_PORT_NUMBER) + MIN_PORT_NUMBER;

			try {
				return new ServerSocket(port);
			} catch (IOException ignored) { }
		}
	}
}
