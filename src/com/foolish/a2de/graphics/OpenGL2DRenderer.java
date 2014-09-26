package com.foolish.a2de.graphics;

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

import com.foolish.a2de.OpenGL2DActivity;
import com.foolish.a2de.physics.SimpleRectanglePhysics;
import com.foolish.opengl_2d_engine.R;

public class OpenGL2DRenderer implements Renderer {

	protected static final int DEFAULT_BG_COLOR = Color.argb(255, 0, 0, 0);
	protected int mClearColor;

	protected final float mProjectionMatrix[] = new float[16];
	protected final float mViewMatrix[] = new float[16];
	protected final float mRotationMatrix[] = new float[16];
	protected final float mMVPMatrix[] = new float[16];

	protected volatile float mCameraX = 0f, mCameraY = 0f, mCameraZ = 0f;

	protected Context mContext;

	public double angle = 0d;

	protected int mWidth, mHeight;
	protected float mAspectRatio;

	public Sprite mSprite;
	private Rectangle mFloor, mLeftWall, mRightWall, mPlat1, mPlat2, mPlat3;

	public OpenGL2DRenderer(Context context) {
		mContext = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		clear(DEFAULT_BG_COLOR);

		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.player);

		mSprite = new Sprite(-.25f, .75f, .19f, .25f, bmp);
		mSprite.init();

		mFloor = new Rectangle(-1.3f, -0.7f, 2.6f, 0.2f);
		mFloor.init();
		mFloor.setColor(128, 128, 128, 255);

		mLeftWall = new Rectangle(-1.3f, -0.2f, 0.2f, 0.5f);
		mLeftWall.init();
		mLeftWall.setColor(128, 128, 128, 255);

		mRightWall = new Rectangle(1.1f, -0.2f, 0.2f, 0.5f);
		mRightWall.init();
		mRightWall.setColor(128, 128, 128, 255);

		mPlat1 = new Rectangle(-1.0f, -0.4f, 0.3f, 0.1f);
		mPlat1.init();
		mPlat1.setColor(128, 128, 128, 255);

		mPlat2 = new Rectangle(0.7f, -0.1f, 0.3f, 0.1f);
		mPlat2.init();
		mPlat2.setColor(128, 128, 128, 255);

		mPlat3 = new Rectangle(0.1f, 0.2f, 0.3f, 0.1f);
		mPlat3.init();
		mPlat3.setColor(128, 128, 128, 255);

		mSprite.setPhysics(new SimpleRectanglePhysics(mFloor, mLeftWall,
				mRightWall, mPlat1, mPlat2, mPlat3));
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		mWidth = width;
		mHeight = height;

		mAspectRatio = (float) width / height;
		Matrix.frustumM(mProjectionMatrix, 0, -mAspectRatio, mAspectRatio, -1,
				1, 3, 7);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 0f);
		Matrix.translateM(mViewMatrix, 0, mCameraX, mCameraY, mCameraZ);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		if (mSprite.mPos.y < -1.0f) {
			mSprite.moveTo(0.0f, 0.5f);
		}

		mSprite.setSpeed(OpenGL2DActivity.speedX, mSprite.speed().y);

		mFloor.draw(mMVPMatrix);
		mLeftWall.draw(mMVPMatrix);
		mRightWall.draw(mMVPMatrix);
		mPlat1.draw(mMVPMatrix);
		mPlat2.draw(mMVPMatrix);
		mPlat3.draw(mMVPMatrix);
		mSprite.draw(mMVPMatrix);
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
