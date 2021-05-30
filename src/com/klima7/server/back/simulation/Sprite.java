package com.klima7.server.back.simulation;

class Sprite {

	private double x, y;
	private final double width, height;

	public Sprite(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public boolean collide(Sprite other) {
		return (x < other.x + other.width &&
				x + width > other.x &&
				y < other.y + other.height &&
				y + height > other.y);
	}

	public double getVerticalCollisionOffset(Sprite other) {
		double center1 = y+height/2;
		double center2 = other.y+other.height/2;
		return (center2-center1) / (getHeight() / 2);
	}

	public void alignTopEdgeTo(Sprite other) {
		setY(other.getY()+other.getHeight());
	}

	public void alignBottomEdgeTo(Sprite other) {
		setY(other.getY()-getHeight());
	}
}
