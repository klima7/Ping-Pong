package com.klima7.server.back;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class Server implements TcpManager.ConnectionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	private static Server instance;

	private boolean running;

	private TcpManager tcpManager;
	private UdpManager udpManager;
	private QueueManager queueManager;
	private InviteManager inviteManager;

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
		inviteManager = new InviteManager(nick);

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

	public CompletableFuture<Client> takeFromQueueAsync() {
		return CompletableFuture.supplyAsync(() -> takeFromQueue());
	}

	public Client takeFromQueue() {
		while(true) {
			try {
				Socket socket = queueManager.pop();
				String client_nick = inviteManager.invite(socket);
				if (client_nick != null) {
					return new Client(client_nick, socket);
				}
			} catch (InterruptedException | IOException ignored) {}
		}
	}
}
