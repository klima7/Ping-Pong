package com.klima7.simulation;

import com.klima7.app.Constants;

public class Player extends Sprite {

	private Simulation simulation;

	public Player(Simulation simulation, double x) {
		super(x, 0, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
		this.simulation = simulation;
	}

	public synchronized void setPosition(int newPosition) {
		Ball ball = simulation.getBall();
		boolean collideBefore = this.collide(ball);

		int shift = newPosition - getPosition();
		setY(newPosition);

		if(!collideBefore && this.collide(ball)) {
			if(shift > 0) {
				ball.alignTopEdgeTo(this);
				ball.setVy(Ball.SPEED/2);
			}
			else {
				ball.alignBottomEdgeTo(this);
				ball.setVy(-Ball.SPEED/2);
			}
		}

	}

	public int getPosition() {
		return (int)getY();
	}
}
