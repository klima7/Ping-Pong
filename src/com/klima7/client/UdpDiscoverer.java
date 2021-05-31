package com.klima7.client;

import com.klima7.app.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UdpDiscoverer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WaitingAssistant.class);

	public static final int DISCOVERY_TIME = 300;

	public static List<Offer> discover() throws IOException {
		LOGGER.info("Discovering started");

		List<Offer> offers = new ArrayList<>();
		MulticastSocket socket = createSocket();
		sendDiscover();

		long startTime = System.currentTimeMillis();
		do {
			Offer offer = receiveMessage(socket);
			if(offer != null) {
				offers.add(offer);
			}
		} while(System.currentTimeMillis() - startTime < DISCOVERY_TIME);

		closeSocketAndLeaveGroup(socket);
		return offers;
	}

	public static CompletableFuture<List<Offer>> discoverAsync() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return discover();
			} catch (IOException e) {
				return null;
			}
		});
	}

	private static MulticastSocket createSocket() throws IOException {
		MulticastSocket socket = new MulticastSocket(Constants.DISCOVERY_PORT);
		socket.setReuseAddress(true);
		socket.setSoTimeout(100);

		InetSocketAddress groupAddress = new InetSocketAddress(InetAddress.getByName(Constants.DISCOVERY_GROUP), 0);
		socket.joinGroup(groupAddress, NetworkInterface.getByName("wlan0"));

		return socket;
	}

	private static Offer receiveMessage(MulticastSocket socket) throws IOException {
		byte[] receiveData = new byte[100];
		DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
		try {
			socket.receive(packet);
		} catch(SocketTimeoutException e) {
			return null;
		}
		String message = new String(packet.getData(), packet.getOffset(), packet.getLength());

		String[] parts = message.split("\\s");
		if(parts.length != 3 || !parts[0].equals("OFFER"))
			return null;

		try {
			int port =  Integer.parseInt(parts[1]);
			LOGGER.info("Offer received");
			return new Offer(packet.getAddress(), port, parts[2]);
		} catch(NumberFormatException e) {
			return null;
		}
	}

	private static void closeSocketAndLeaveGroup(MulticastSocket socket) throws IOException {
		InetSocketAddress groupAddress = new InetSocketAddress(InetAddress.getByName(Constants.DISCOVERY_GROUP), 0);
		socket.leaveGroup(groupAddress, NetworkInterface.getByName("wlan0"));
		socket.close();
	}

	private static void sendDiscover() throws IOException {
		LOGGER.info("Sending DISCOVER");

		InetAddress inetAddress = InetAddress.getByName(Constants.DISCOVERY_GROUP);

		DatagramSocket socket = new DatagramSocket();
		socket.setBroadcast(true);

		String message = "DISCOVER";
		byte[] buffer = message.getBytes();

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, Constants.DISCOVERY_PORT);
		socket.send(packet);
		socket.close();
	}
}
