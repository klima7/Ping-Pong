package com.klima7.simulation;

import static com.klima7.app.Constants.*;

public class Ball extends Sprite {

	public static final double SPEED = 150;
	public static final double BOUNCE_FACTOR = 90;

	private double vx, vy;
	private Simulation simulation;

	public Ball(Simulation simulation) {
		super(0, 0, BALL_SIZE, BALL_SIZE);
		this.simulation = simulation;
		centerPosition();
	}

	public void update(long millis) {
		Player[] players = {simulation.getClient(), simulation.getServer()};

		for(Player player : players) {
			if(this.collide(player)) {
				double offset = player.getVerticalCollisionOffset(this);
				vx *= -1;
				setVy(offset * BOUNCE_FACTOR);
			}
		}

		setX(getX() + vx*millis/1000);
		setY(getY() + vy*millis/1000);

		if(getY() < 0) {
			setY(0);
			setVy(-getVy());
		}

		if(getY()+getHeight() > MAP_HEIGHT) {
			setY(MAP_HEIGHT - getHeight());
			setVy(-getVy());
		}
	}

	public void centerPosition() {
		setX(MAP_WIDTH / 2 - BALL_SIZE / 2);
		setY(MAP_HEIGHT / 2 - BALL_SIZE / 2);
	}

	public void setVx(double xVelocity) {
		System.out.println("xVelocity");
		vx = xVelocity;
		vy = Math.sqrt(SPEED*SPEED - xVelocity*xVelocity) * Math.signum(vy);
	}

	public void setVy(double yVelocity) {
		System.out.println("yVelocity");
		vy = yVelocity;
		vx = Math.sqrt(SPEED*SPEED - yVelocity*yVelocity) * Math.signum(vx);
	}

	public double getVx() {
		return vx;
	}

	public double getVy() {
		return vy;
	}
}
