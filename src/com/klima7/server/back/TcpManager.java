package com.klima7.server.back;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public class TcpManager  {

	public static final int MIN_PORT_NUMBER = 1;
	public static final int MAX_PORT_NUMBER = 65_535;

	private final ServerSocket serverSocket;

	public TcpManager() {
		this.serverSocket = randomSocket();
	}

	public int getPort() {
		return serverSocket.getLocalPort();
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
