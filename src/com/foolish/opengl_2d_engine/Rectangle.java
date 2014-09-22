package com.foolish.opengl_2d_engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Rectangle extends Shape {

	protected static final short INDEX_ORDER[] = { 0, 1, 2, 0, 2, 3 };
	protected static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

	protected ShortBuffer mIndexBuffer;

	public Rectangle(float x, float y, float w, float h) {
		super(x, y, w, h, 4);
	}

	public Rectangle(Vector2f pos, float w, float h) {
		super(pos, w, h, 4);
	}

	@Override
	protected void init() {
		super.init();

		ByteBuffer vb = ByteBuffer.allocateDirect(VERTEX_COUNT
				* COORDS_PER_VERTEX * 4);
		vb.order(ByteOrder.nativeOrder());

		Vector3f topLeft = new Vector3f(mPos.x, mPos.y, 0);
		Vector3f bottomLeft = new Vector3f(mPos.x, mPos.y - mHeight, 0);
		Vector3f bottomRight = new Vector3f(mPos.x + mWidth, mPos.y - mHeight,
				0);
		Vector3f topRight = new Vector3f(mPos.x + mWidth, mPos.y, 0);

		mVertexBuffer = vb.asFloatBuffer();
		mVertexBuffer.put(new float[] { topLeft.x, topLeft.y, topLeft.z,
				bottomLeft.x, bottomLeft.y, bottomLeft.z, bottomRight.x,
				bottomRight.y, bottomRight.z, topRight.x, topRight.y,
				topRight.z });
		mVertexBuffer.position(0);

		ByteBuffer ib = ByteBuffer.allocateDirect(INDEX_ORDER.length * 2);
		ib.order(ByteOrder.nativeOrder());
		mIndexBuffer = ib.asShortBuffer();
		mIndexBuffer.put(INDEX_ORDER);
		mIndexBuffer.position(0);
	}

	@Override
	public void draw(float[] mvpMatrix) {
		Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, mModelMatrix, 0);
		
		GLES20.glUseProgram(mProgram);

		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, VERTEX_STRIDE, mVertexBuffer);

		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		GLES20.glUniform4fv(mColorHandle, 1, mShapeColor, 0);

		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		OpenGL2DRenderer.checkGlError("glGetUniformLocation");

		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		OpenGL2DRenderer.checkGlError("glUniformMatrix4fv");

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, INDEX_ORDER.length,
				GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
