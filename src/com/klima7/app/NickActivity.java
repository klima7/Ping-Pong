package com.klima7.app;

import javax.swing.*;
import java.awt.*;

public class NickActivity extends JPanel {

	private App app;

	public NickActivity(App app) {
		this.app = app;
		setLayout(new FlowLayout());
		add(new JButton("Nick"));
		System.out.println("Nick activity");
	}
}
