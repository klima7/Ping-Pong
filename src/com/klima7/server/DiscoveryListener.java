package com.klima7.server;

import java.io.IOException;
import java.net.*;


public class DiscoveryListener extends Thread {

	private MulticastSocket socket;
	private final InetSocketAddress groupAddress;
	private boolean running;

	public DiscoveryListener(int port, String group) throws IOException {
		if(socket != null)
			throw new IllegalStateException("Already listening");

		this.running = true;

		try {
			groupAddress = new InetSocketAddress(group, port);
		} catch(IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid group address");
		}

		socket = new MulticastSocket(port);
		socket.setReuseAddress(true);
		socket.joinGroup(groupAddress, NetworkInterface.getByName("wlan0"));
	}

	public void stopListening() {
		running = false;
	}

	@Override
	public void run() {
		byte[] receiveData = new byte[100];


		while(running) {
			try {
				DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
				socket.receive(packet);
				String sentence = new String(packet.getData(), packet.getOffset(), packet.getLength());
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		try {
			socket.leaveGroup(groupAddress, NetworkInterface.getByName("wlan0"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		socket.close();
	}
}
