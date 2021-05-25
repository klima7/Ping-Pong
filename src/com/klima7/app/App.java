package com.klima7.app;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

	public static final String TITLE = "Ping-Pong";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private final JPanel basePanel;

	public App() {
		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		add(basePanel);
	}

	public void setActivity(JPanel activity) {
		basePanel.removeAll();
		basePanel.add(activity, BorderLayout.CENTER);
		revalidate();
	}

	public static void main(String[] args) {
		App app = new App();
		app.setActivity(new GameActivity(app));
		app.setVisible(true);
	}
}
