package com.foolish.opengl_2d_engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

public class OpenGL2DRenderer implements Renderer {

	protected static final int DEFAULT_BG_COLOR = Color.argb(255, 0, 0, 0);
	protected int mClearColor;

	protected final float mProjectionMatrix[] = new float[16];
	protected final float mViewMatrix[] = new float[16];
	protected final float mRotationMatrix[] = new float[16];
	protected final float mMVPMatrix[] = new float[16];

	protected volatile float mCameraX = 0f, mCameraY = 0f, mCameraZ = 0f;

	protected Context mContext;

	// private Shape mTriangle;
	// private Shape mRectangle;
	private Sprite mBackground;
	private Sprite mSprite;

	public OpenGL2DRenderer(Context context) {
		mContext = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		clear(Color.RED);

		// mTriangle = new Triangle(-1.5f, 0.5f, 1, 1);
		// mTriangle.setColor(255, 255, 0, 255);
		//
		// mRectangle = new Rectangle(0, 0.3f, 1, 1);
		// mRectangle.setColor(255, 0, 0, 255);
		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.ic_launcher);
		Bitmap bmpBg = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.tulips);
		mBackground = new Sprite(-1.6f, 1, 3.2f, 2, bmpBg);
		mBackground.init();
		mSprite = new Sprite(0, 0.5f, .5f, .5f, bmp);
		mSprite.init();
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

		// mTriangle.draw(mMVPMatrix);
		// mRectangle.draw(mMVPMatrix);
		mBackground.draw(mMVPMatrix);
		// mSprite.draw(mMVPMatrix);
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

	/**
	 * Utility method for debugging OpenGL calls. Provide the name of the call
	 * just after making it:
	 * 
	 * <pre>
	 * mColorHandle = GLES20.glGetUniformLocation(mProgram, &quot;vColor&quot;);
	 * MyGLRenderer.checkGlError(&quot;glGetUniformLocation&quot;);
	 * </pre>
	 * 
	 * If the operation is not successful, the check throws an error.
	 * 
	 * @param glOperation
	 *            - Name of the OpenGL call to check.
	 */
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(OpenGL2DRenderer.class.getName(), glOperation + ": glError "
					+ error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

}
