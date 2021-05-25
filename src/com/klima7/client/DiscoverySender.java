package com.klima7.client;

import com.klima7.app.Constants;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DiscoverySender {

	public static final int DISCOVERY_TIME = 1000;

	public static List<InetSocketAddress> discover() throws IOException {
		List<InetSocketAddress> addresses = new ArrayList<>();
		MulticastSocket socket = createSocket();
		sendDiscover();

		long startTime = System.currentTimeMillis();
		do {
			InetSocketAddress address = receiveMessage(socket);
			if(address != null)
				addresses.add(address);
		} while(System.currentTimeMillis() - startTime < DISCOVERY_TIME);

		closeSocketAndLeaveGroup(socket);
		return addresses;
	}

	public static CompletableFuture<List<InetSocketAddress>> discoverAsync() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return discover();
			} catch (IOException e) {
				e.printStackTrace();
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

	private static InetSocketAddress receiveMessage(MulticastSocket socket) throws IOException {
		byte[] receiveData = new byte[100];
		DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
		try {
			socket.receive(packet);
		} catch(SocketTimeoutException e) {
			return null;
		}
		String message = new String(packet.getData(), packet.getOffset(), packet.getLength());

		if(!message.startsWith("OFFER "))
			return null;

		String portPart = message.substring(6);
		try {
			int port =  Integer.parseInt(portPart);
			return new InetSocketAddress(packet.getAddress(), port);
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
