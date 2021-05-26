package com.klima7.app.gui;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

	public static final String TITLE = "Ping-Pong";
	public static final int WIDTH = 700;
	public static final int HEIGHT = 500;

	private final JPanel basePanel;

	public App(Activity startActivity) {
		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		add(basePanel);

		setActivity(startActivity);
		setVisible(true);
	}

	public void setActivity(Activity activity) {
		activity.setContext(this);
		Activity oldActivity = getActivity();
		if(oldActivity != null)
			oldActivity.onStop();
		basePanel.removeAll();
		activity.onStart();
		basePanel.add(activity, BorderLayout.CENTER);
		revalidate();
	}

	public Activity getActivity() {
		Component[] components = basePanel.getComponents();
		if(components.length == 0)
			return null;
		Activity activity = (Activity)basePanel.getComponent(0);
		return activity;
	}

	public static void main(String[] args) {
		new App(new NickActivity());
	}
}
