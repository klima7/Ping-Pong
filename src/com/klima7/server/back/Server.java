package com.klima7.server.back;

import java.io.IOException;
import java.net.Socket;

public class Server implements TcpManager.ConnectionListener {

	private static Server instance;

	private String nick;
	private boolean running;

	private TcpManager tcpManager;
	private UdpManager udpManager;
	private QueueManager queueManager;
	private InviteManager inviteManager;
	private GameManager gameManager;

	public static Server getInstance() {
		if(instance == null)
			instance = new Server();
		return instance;
	}

	public void start() throws IOException {
		if(running)
			return;

		tcpManager = new TcpManager(this);
		udpManager = new UdpManager(tcpManager.getPort(), nick);
		queueManager = new QueueManager();
		inviteManager = new InviteManager();
		gameManager = new GameManager();

		udpManager.start();
		tcpManager.start();

		running = true;
	}

	public void stop() throws IOException {
		if(!running)
			return;

		udpManager.stopListening();
		tcpManager.stopListening();
		running = false;
	}

	@Override
	public void onConnection(Socket socket) {
		queueManager.add(socket);
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
}
