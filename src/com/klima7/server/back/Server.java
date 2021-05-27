package com.klima7.server.back;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class Server implements TcpManager.ConnectionListener, GameManager.GameManagerListener, InviteManager.InviteManagerListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

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

		LOGGER.info("Starting server");

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

		LOGGER.info("Stopping server");

		udpManager.stopListening();
		tcpManager.stopListening();
		running = false;
	}

	@Override
	public synchronized void onConnection(Socket socket) {
		LOGGER.info("onConnection triggered");
		queueManager.add(socket);
		inviteManager.invite(socket);
	}

	@Override
	public void onNickInvalid(Socket socket) {
		LOGGER.info("onNickInvalid triggered");
	}

	@Override
	public void onNickValid(Client client) {
		LOGGER.info("onNickValid triggered");
		gameManager.startGame(client);
	}

	@Override
	public void onInviteError(Socket socket) {
		LOGGER.info("onInviteError triggered");
	}

	@Override
	public synchronized void onGameFinished() {
		LOGGER.info("onGameFinished triggered");
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
