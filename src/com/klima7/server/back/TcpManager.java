package com.klima7.server.back;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TcpManager extends Thread {

	public static final int MIN_PORT_NUMBER = 1;
	public static final int MAX_PORT_NUMBER = 65_535;

	private boolean running;
	private final ConnectionListener listener;
	private final ServerSocket serverSocket;

	public TcpManager(ConnectionListener listener) {
		this.listener = listener;
		this.serverSocket = randomSocket();
	}

	@Override
	public void run() {
		running = true;
		while(running) {
			try {
				Socket socket = serverSocket.accept();
				listener.onConnection(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	public void stopListening() {
		running = false;
	}

	public interface ConnectionListener {
		void onConnection(Socket socket);
	}
}
