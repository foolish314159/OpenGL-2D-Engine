package com.foolish.a2de.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.foolish.a2de.physics.IPhysics2D;
import com.foolish.opengl_2d_engine.R;

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

	public double angle = 0d;

	protected int mWidth, mHeight;
	protected float mAspectRatio;

	private Sprite mBackground;
	public Sprite mSprite;
	private Rectangle mShrektangle;

	public OpenGL2DRenderer(Context context) {
		mContext = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		clear(DEFAULT_BG_COLOR);

		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.ic_launcher);
		Bitmap bmpBg = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.tulips);

		mBackground = new Sprite(-1.6f, 1, 3.2f, 2, bmpBg);
		mBackground.init();
		mSprite = new Sprite(-.25f, .75f, .5f, .5f, bmp);
		mSprite.init();

		mShrektangle = new Rectangle(-1.3f, -0.7f, 2.6f, 0.2f);
		mShrektangle.init();
		System.out.println(mShrektangle.center().x + "|"
				+ mShrektangle.center().y);

		mSprite.setPhysics(new IPhysics2D() {
			@Override
			public void applyPhysics(Shape shape) {
				mSprite.update();

				if (mSprite.intersects(mShrektangle)) {
					mSprite.translate(-mSprite.speed().x, -mSprite.speed().y);
					mSprite.setSpeed(mSprite.speed().x, 0.0f);
				} else {
					mSprite.setSpeed(mSprite.speed().x, -0.01f);
				}
			}
		});
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

		mBackground.draw(mMVPMatrix);
		mSprite.draw(mMVPMatrix);
		mShrektangle.draw(mMVPMatrix);
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
