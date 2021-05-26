package com.klima7.server.back;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class QueueManager {

	private final Queue<Socket> queue = new LinkedList<>();

	public synchronized void add(Socket socket) {
		queue.add(socket);
		int position = queue.size();
		sendPosition(socket, position);
	}

	public synchronized Socket pop() {
		Socket socket = queue.poll();
		sendCurrentPositionToAll();
		return socket;
	}

	private void sendCurrentPositionToAll() {
		int position = 1;
		for(Socket socket : queue) {
			sendPosition(socket, position++);
		}
	}

	private void sendPosition(Socket socket, int position) {
		try {
			OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
			String message = "POSITION " + position;
			output.write(message);
			output.flush();
		} catch (IOException e) {
			// Można potem usunąć
			e.printStackTrace();
		}
	}

}
