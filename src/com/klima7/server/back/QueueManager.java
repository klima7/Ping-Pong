package com.klima7.server.back;

import com.klima7.app.back.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class QueueManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueManager.class);

	private final BlockingQueue<Socket> queue = new LinkedBlockingDeque<>();

	public void add(Socket socket) {
		LOGGER.info("Adding socket to queue");
		queue.add(socket);
		int position = queue.size();
		sendPosition(socket, position);
	}

	public Socket pop() throws InterruptedException {
		Socket socket = queue.take();
		LOGGER.info("Taking socket from queue");
		sendCurrentPositionToAll();
		return socket;
	}

	private void sendCurrentPositionToAll() {
		LOGGER.debug("Sending new position to all sockets");
		int position = 1;
		for(Socket socket : queue) {
			sendPosition(socket, position++);
		}
	}

	private void sendPosition(Socket socket, int position) {
		LOGGER.debug("Sending new position " + position);
		try {
			OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
			String message = "POSITION " + position + Constants.COMMAND_END;
			output.write(message);
			output.flush();
		} catch (IOException e) {
			LOGGER.warn("Exception during sending position", e);
		}
	}

}
