package com.klima7.server.back;

import com.klima7.app.back.Constants;

import java.io.IOException;
import java.net.*;


public class UdpManager extends Thread {

	private int offeringPort;
	private String nick;

	private boolean running;
	private MulticastSocket socket;

	public UdpManager(int offeringPort, String nick) throws IOException {
		this.offeringPort = offeringPort;
		this.nick = nick;
		this.running = true;

		socket = new MulticastSocket(Constants.DISCOVERY_PORT);
		socket.setReuseAddress(true);

		InetSocketAddress groupAddress = new InetSocketAddress(InetAddress.getByName(Constants.DISCOVERY_GROUP), 0);
		socket.joinGroup(groupAddress, NetworkInterface.getByName("wlan0"));
	}

	public void stopListening() {
		running = false;
	}

	@Override
	public void run() {
		while(running)
			receiveAndProcessDatagram();
		closeSocketAndLeaveGroup();
	}

	private void receiveAndProcessDatagram() {
		try {
			String message = receiveMessage();
			if(isDiscoveryMessage(message)) {
				sendOffer();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isDiscoveryMessage(String message) {
		return message.equals("DISCOVER");
	}

	private String receiveMessage() throws IOException {
		byte[] receiveData = new byte[100];
		DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
		socket.receive(packet);
		return new String(packet.getData(), packet.getOffset(), packet.getLength());
	}

	private void sendOffer() throws IOException {
		InetAddress inetAddress = InetAddress.getByName(Constants.DISCOVERY_GROUP);

		DatagramSocket socket = new DatagramSocket();
		socket.setBroadcast(true);

		String message = "OFFER " + offeringPort + " " + nick;
		byte[] buffer = message.getBytes();

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, Constants.DISCOVERY_PORT);
		socket.send(packet);
		socket.close();
	}

	private void closeSocketAndLeaveGroup() {
		try {
			InetSocketAddress groupAddress = new InetSocketAddress(InetAddress.getByName(Constants.DISCOVERY_GROUP), 0);
			socket.leaveGroup(groupAddress, NetworkInterface.getByName("wlan0"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
	}
}
