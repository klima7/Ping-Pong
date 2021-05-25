package com.klima7.server;

import java.io.IOException;
import java.util.Random;

public class Server {

	public static final int MIN_PORT_NUMBER = 1;
	public static final int MAX_PORT_NUMBER = 65_535;

	private static Server instance;

	private final int randomPort;
	private DiscoveryListener listener;

	public static Server getInstance() {
		if(instance == null)
			instance = new Server();
		return instance;
	}

	private Server() {
		this.randomPort = randomizePort();
	}

	public void startDiscoveryListen() throws IOException {
		if(listener != null)
			throw new IOException("Already listening");
		listener = new DiscoveryListener(randomPort);
		listener.start();
	}

	public void stopDiscoveryListen() {
		listener.stopListening();
		listener = null;
	}

	private int randomizePort() {
		Random random = new Random();
		int port = random.nextInt(MAX_PORT_NUMBER-MIN_PORT_NUMBER) + MIN_PORT_NUMBER;
		return port;
	}
}
