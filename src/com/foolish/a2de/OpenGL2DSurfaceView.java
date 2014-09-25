package com.foolish.a2de;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.foolish.a2de.graphics.OpenGL2DRenderer;

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

}
