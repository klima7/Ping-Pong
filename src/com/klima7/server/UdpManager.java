package com.klima7.server;

import com.klima7.app.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;


public class UdpManager extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(UdpManager.class);

	private final int offeringPort;
	private final String nick;

	private final MulticastSocket socket;

	public UdpManager(int offeringPort, String nick) throws IOException {
		this.offeringPort = offeringPort;
		this.nick = nick;

		socket = new MulticastSocket(Constants.DISCOVERY_PORT);
		socket.setReuseAddress(true);
		socket.setSoTimeout(100);

		InetSocketAddress groupAddress = new InetSocketAddress(InetAddress.getByName(Constants.DISCOVERY_GROUP), 0);
		socket.joinGroup(groupAddress, NetworkInterface.getByName("wlan0"));
	}

	public void stopListening() {
		interrupt();
	}

	@Override
	public void run() {
		LOGGER.info("Udp listening started");
		try {
			while(true)
				receiveAndProcessDatagram();
		} catch (InterruptedException e) { }
		closeSocketAndLeaveGroup();
		LOGGER.info("Udp listening stopped");
	}

	private void receiveAndProcessDatagram() throws InterruptedException {
		try {
			String message = receiveMessage();
			LOGGER.debug("Datagram received: " + message);
			if(message.equals("DISCOVER")) {
				sendOffer();
			}
		} catch(IOException e) {
			LOGGER.warn("Exception during receiving occurred", e);
		}
	}

	private String receiveMessage() throws IOException, InterruptedException {
		byte[] receiveData = new byte[100];
		DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
		while(true) {
			try {
				socket.receive(packet);
				return new String(packet.getData(), packet.getOffset(), packet.getLength());
			} catch (SocketTimeoutException e) {
				if (isInterrupted())
					throw new InterruptedException();
			}
		}
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
		try {
			InetSocketAddress groupAddress = new InetSocketAddress(InetAddress.getByName(Constants.DISCOVERY_GROUP), 0);
			socket.leaveGroup(groupAddress, NetworkInterface.getByName("wlan0"));
		} catch (IOException e) {
			LOGGER.warn("Exception during leaving udp group occurred", e);
		}
		socket.close();
	}
}
