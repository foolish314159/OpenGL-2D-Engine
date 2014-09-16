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

		mVertexBuffer = bb.asFloatBuffer();
		mVertexBuffer.put(new float[] { mPos.x + mWidth / 2, mPos.y, 0, mPos.x,
				mPos.y - mHeight, 0, mPos.x + mWidth, mPos.y - mHeight, 0 });
		mVertexBuffer.position(0);
	}

}
