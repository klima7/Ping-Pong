package com.klima7.app;

import javax.swing.*;

public class Activity extends JPanel {

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

	public void onStart() {};

	public void onStop() {};

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
