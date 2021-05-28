package com.klima7.server.back;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class Server implements TcpManager.ConnectionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	private static Server instance;

	private boolean running;

	private TcpManager tcpManager;
	private UdpManager udpManager;
	private QueueManager queueManager;

	public static Server getInstance() {
		if(instance == null)
			instance = new Server();
		return instance;
	}

	public void start(String nick) throws IOException {
		if(running)
			return;

		LOGGER.info("Starting server");

		tcpManager = new TcpManager(this);
		udpManager = new UdpManager(tcpManager.getPort(), nick);
		queueManager = new QueueManager();

		udpManager.start();
		tcpManager.start();

		running = true;
	}

	public void stop() throws IOException {
		if(!running)
			return;

		LOGGER.info("Stopping server");

		udpManager.stopListening();
		tcpManager.stopListening();
		running = false;
	}

	@Override
	public synchronized void onConnection(Socket socket) {
		LOGGER.info("onConnection triggered");
		queueManager.add(socket);
	}
}
