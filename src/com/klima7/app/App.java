package com.klima7.app;

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
		basePanel.removeAll();
		basePanel.add(activity, BorderLayout.CENTER);
		revalidate();
	}

	public static void main(String[] args) {
		new App(new NickActivity());
	}
}
