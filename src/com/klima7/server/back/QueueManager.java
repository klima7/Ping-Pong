package com.klima7.server.back;

import com.klima7.app.back.Constants;
import com.klima7.client.back.WaitingAssistant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class QueueManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueManager.class);

	private final Queue<Socket> queue = new LinkedList<>();

	public synchronized void add(Socket socket) {
		LOGGER.info("Adding socket to queue");
		queue.add(socket);
		int position = queue.size();
		sendPosition(socket, position);
	}

	public synchronized Socket pop() {
		LOGGER.info("Popping socket from queue");
		Socket socket = queue.poll();
		sendCurrentPositionToAll();
		return socket;
	}

	public boolean isEmpty() {
		return queue.isEmpty();
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
