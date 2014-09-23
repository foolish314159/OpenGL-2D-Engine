package com.foolish.opengl_2d_engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class OpenGL2DSurfaceView extends GLSurfaceView {

	protected OpenGL2DRenderer mRenderer;
	float speedX = 0.0f;

	public OpenGL2DSurfaceView(Context context) {
		super(context);
		init(context);
	}

	public OpenGL2DSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	protected void init(Context context) {
		setEGLContextClientVersion(2);
		mRenderer = new OpenGL2DRenderer(context);
		setRenderer(mRenderer);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			speedX = 0.05f;
			break;
		case MotionEvent.ACTION_UP:
			speedX = 0.0f;
			break;
		}

		if (x < getWidth() / 2)
			mRenderer.mSprite.setSpeed(-speedX, mRenderer.mSprite.speed().y);
		else
			mRenderer.mSprite.setSpeed(speedX, mRenderer.mSprite.speed().y);

		return true;

	}
}
