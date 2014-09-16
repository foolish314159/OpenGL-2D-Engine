package com.foolish.opengl_2d_engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class OpenGL2DSurfaceView extends GLSurfaceView {

	protected Renderer mRenderer;

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

}
