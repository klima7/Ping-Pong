package com.klima7.client.back;

import java.io.*;
import java.net.InetAddress;
import java.util.Properties;

public class LastServer {

	private File propertiesFile;

	public LastServer() {
		String path = System.getProperty("user.home") + File.separator + "PingPong";
		File pingPongDirectory = new File(path);
		if(!pingPongDirectory.exists())
			pingPongDirectory.mkdirs();
		propertiesFile = new File(pingPongDirectory, "last-server.xml");
	}

	public void set(Offer lastServer) throws IOException {
		Properties properties = new Properties();
		properties.setProperty("ip", lastServer.getAddress().getHostAddress());
		properties.setProperty("port", String.valueOf(lastServer.getPort()));
		OutputStream output = new FileOutputStream(propertiesFile);
		properties.storeToXML(output, "Last server");
		output.flush();
		output.close();
	}

	public Offer get() throws IOException {
		Properties properties = new Properties();
		InputStream input = new FileInputStream(propertiesFile);
		properties.loadFromXML(input);
		InetAddress address = InetAddress.getByName(properties.getProperty("ip"));
		int port = Integer.parseInt(properties.getProperty("port"));
		return new Offer(address, port, null);
	}
}
