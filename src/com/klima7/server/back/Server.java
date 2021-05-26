package com.klima7.server.back;

import java.io.IOException;
import java.net.Socket;

public class Server implements TcpManager.ConnectionListener, GameManager.GameManagerListener, InviteManager.InviteManagerListener {

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
		inviteManager = new InviteManager(this, nick);
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
	public synchronized void onConnection(Socket socket) {
		System.out.println("onConnection");
		queueManager.add(socket);

//		inviteManager.invite(socket);

//		if(!gameManager.isGameInProgress()) {
//			onGameFinished();
//		}
	}

	@Override
	public void onNickInvalid(Socket socket) {
		System.out.println("Server.onNickInvalid");
	}

	@Override
	public void onNickValid(Client client) {
		System.out.println("Server.onNickValid " + client.getNick());
	}

	@Override
	public void onInviteError(Socket socket) {
		System.out.println("Server.onInviteError");
	}

	@Override
	public synchronized void onGameFinished() {
		System.out.println("onGameFinished");

		if(queueManager.isEmpty())
			return;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
}
