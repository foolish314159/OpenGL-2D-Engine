package com.foolish.opengl_2d_engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Triangle extends Shape {

	public Triangle(float x, float y, float w, float h) {
		super(x, y, w, h, 3);
	}

	public Triangle(Vector2f pos, float w, float h) {
		super(pos, w, h, 3);
	}

	@Override
	protected void init() {
		super.init();

		ByteBuffer bb = ByteBuffer.allocateDirect(VERTEX_COUNT
				* COORDS_PER_VERTEX * 4);
		bb.order(ByteOrder.nativeOrder());

		Vector3f top = new Vector3f(mPos.x + mWidth / 2, mPos.y, 0);
		Vector3f bottomLeft = new Vector3f(mPos.x, mPos.y - mHeight, 0);
		Vector3f bottomRight = new Vector3f(mPos.x + mWidth, mPos.y - mHeight,
				0);

		mVertexBuffer = bb.asFloatBuffer();
		mVertexBuffer.put(new float[] { top.x, top.y, top.z, bottomLeft.x,
				bottomLeft.y, bottomLeft.z, bottomRight.x, bottomRight.y,
				bottomRight.z });
		mVertexBuffer.position(0);
	}

}
