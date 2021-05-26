package com.klima7.app.gui;

import com.klima7.server.back.TcpManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Activity extends JPanel {

	private static final Logger LOGGER = LoggerFactory.getLogger(TcpManager.class);

	private App context;

	public Activity() {
		initUI();
	}

	public App getContext() {
		return context;
	}

	public void setContext(App context) {
		this.context = context;
	}

	public void initUI() {};

	public void onStart() {
		LOGGER.info("Starting activity " + getClass().getSimpleName());
	};

	public void onStop() {
		LOGGER.info("Stopping activity " + getClass().getSimpleName());
	};

	public void startActivity(Activity activity) {
		context.setActivity(activity);
	}

	public void showInfoMessage(String title, String content) {
		JOptionPane.showInternalMessageDialog(context.getContentPane(), content, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public void showErrorMessage(String title, String content) {
		JOptionPane.showInternalMessageDialog(context.getContentPane(), content, title, JOptionPane.ERROR_MESSAGE);
	}
}
