package com.klima7.server.back;

import com.klima7.app.back.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;


public class UdpManager extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(UdpManager.class);

	private final int offeringPort;
	private final String nick;

	private boolean running;
	private final MulticastSocket socket;

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
		LOGGER.info("Running");
		while(running)
			receiveAndProcessDatagram();
		closeSocketAndLeaveGroup();
		LOGGER.info("Quiting thread");
	}

	private void receiveAndProcessDatagram() {
		try {
			String message = receiveMessage();
			LOGGER.debug("Datagram received: " + message);
			if(isDiscoveryMessage(message)) {
				sendOffer();
			}
		} catch(IOException e) {
			LOGGER.warn("Exception during receiving occurred", e);
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
		LOGGER.debug("Sending offer: " + message);
		byte[] buffer = message.getBytes();

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, Constants.DISCOVERY_PORT);
		socket.send(packet);
		socket.close();
	}

	private void closeSocketAndLeaveGroup() {
		LOGGER.info("Closing socket and leaving multicast group");
		try {
			InetSocketAddress groupAddress = new InetSocketAddress(InetAddress.getByName(Constants.DISCOVERY_GROUP), 0);
			socket.leaveGroup(groupAddress, NetworkInterface.getByName("wlan0"));
		} catch (IOException e) {
			LOGGER.warn("Exception during leaving udp group occurred", e);
		}
		socket.close();
	}
}
