package com.foolish.opengl_2d_engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class OpenGL2DRenderer implements Renderer {

	protected static final int DEFAULT_BG_COLOR = Color.argb(255, 0, 0, 0);
	protected int mClearColor;

	protected final float mProjectionMatrix[] = new float[16];
	protected final float mViewMatrix[] = new float[16];
	protected final float mRotationMatrix[] = new float[16];
	protected final float mMVPMatrix[] = new float[16];

	protected volatile float mCameraX = 0f, mCameraY = 0f, mCameraZ = 0f;

	private Shape mTriangle;

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		mClearColor = DEFAULT_BG_COLOR;
		clearColor();

		mTriangle = new Triangle(-1.6f, 1, 3.2f, 2);
		mTriangle.setColor(255, 255, 0, 255);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		float aspectRatio = (float) width / height;
		Matrix.frustumM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1,
				3, 7);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 0f);
		Matrix.translateM(mViewMatrix, 0, mCameraX, mCameraY, mCameraZ);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		mTriangle.draw(mMVPMatrix);
	}

	protected void clearColor() {
		GLES20.glClearColor(Color.red(mClearColor), Color.green(mClearColor),
				Color.blue(mClearColor), Color.alpha(mClearColor));
	}

	public void clear(int color) {
		mClearColor = color;
		clearColor();
	}

	public void moveCamera(float x, float y, float z) {
		mCameraX += x;
		mCameraY += y;
		mCameraZ += z;
	}

	public static int loadShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);

		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

}
