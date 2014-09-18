package com.foolish.opengl_2d_engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class OpenGL2DSurfaceView extends GLSurfaceView {

	protected OpenGL2DRenderer mRenderer;

	public OpenGL2DSurfaceView(Context context) {
		super(context);
		init();
	}

	public OpenGL2DSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	protected void init() {
		setEGLContextClientVersion(2);
		mRenderer = new OpenGL2DRenderer();
		setRenderer(mRenderer);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (x < getWidth() / 2)
				mRenderer.moveCamera(0f, 0f, 0.2f);
			else
				mRenderer.moveCamera(0f, 0f, -0.2f);
			break;
		}
		return true;

	}
}
