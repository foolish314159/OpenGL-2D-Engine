package com.foolish.a2de.physics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.foolish.a2de.graphics.Rectangle;
import com.foolish.a2de.graphics.Shape;
import com.foolish.a2de.graphics.Sprite;

public class SimpleRectanglePhysics implements IPhysics2D {

	private List<Rectangle> mPossibleCollisions;

	public SimpleRectanglePhysics(Rectangle... rects) {
		this(Arrays.asList(rects));
	}

	public SimpleRectanglePhysics(List<Rectangle> rects) {
		mPossibleCollisions = rects;
	}

	public boolean addRect(Rectangle rect) {
		if (mPossibleCollisions == null)
			mPossibleCollisions = new ArrayList<>();

		return mPossibleCollisions.add(rect);
	}

	public boolean removeRect(Rectangle rect) {
		if (mPossibleCollisions == null)
			return false;

		return mPossibleCollisions.remove(rect);
	}

	@Override
	public void applyPhysics(Shape shape) {
		Rectangle rect = (Rectangle) shape;
		rect.update();

		for (Rectangle possibleCollision : mPossibleCollisions)
			if (rect.intersects(possibleCollision)) {
				rect.translate(-rect.speed().x, -rect.speed().y);
				rect.setSpeed(rect.speed().x, 0.0f);
			} else {
				rect.setSpeed(rect.speed().x, -0.01f);
			}
	}

}
