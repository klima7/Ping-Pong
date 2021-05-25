package com.klima7.app;

import javax.swing.*;
import java.awt.*;

public class ModuleActivity extends JPanel {

	private final App app;

	public ModuleActivity(App app) {
		this.app = app;

		setLayout(new FlowLayout());
		JButton button = new JButton("Module");
		add(button);

		button.addActionListener(e -> {
			app.setActivity(new NickActivity(this.app));
			System.out.println("Here");
		});
	}
}
