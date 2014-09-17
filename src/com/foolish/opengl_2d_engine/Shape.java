package com.foolish.opengl_2d_engine;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

public abstract class Shape {

	private static final String VERTEX_SHADER_CODE = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 vPosition;" + "void main() {"
			+ "  gl_Position = uMVPMatrix * vPosition;" + "}";

	private static final String FRAGMENT_SHADER_CODE = "precision mediump float;"
			+ "uniform vec4 vColor;"
			+ "void main() {"
			+ "  gl_FragColor = vColor;" + "}";

	protected static final int COORDS_PER_VERTEX = 3;

	public static class Vector3f {
		public float x, y, z;

		public Vector3f(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	public static class Vector2f {
		public float x, y;

		public Vector2f(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	protected final int VERTEX_COUNT;

	protected int mProgram;
	protected int mPositionHandle;
	protected int mColorHandle;
	protected int mMVPMatrixHandle;

	protected FloatBuffer mVertexBuffer;

	protected float mShapeColor[];

	protected Vector3f mPos;
	protected float mWidth, mHeight;

	public Shape(float x, float y, float w, float h, int vertexCount) {
		mPos = new Vector3f(x, y, 0);
		mWidth = w;
		mHeight = h;
		VERTEX_COUNT = vertexCount;

		init();
	}

	public Shape(Vector2f pos, float w, float h, int vertexCount) {
		this(pos.x, pos.y, w, h, vertexCount);
		init();
	}

	protected void init() {
		mShapeColor = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };

		int vertexShader = OpenGL2DRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
				VERTEX_SHADER_CODE);
		int fragmentShader = OpenGL2DRenderer.loadShader(
				GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);

		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glLinkProgram(mProgram);
	}

	public void draw(float[] mvpMatrix) {
		GLES20.glUseProgram(mProgram);
		
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, 0, mVertexBuffer);
		
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		GLES20.glUniform4fv(mColorHandle, 1, mShapeColor, 0);
		
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

	public void setColor(int r, int g, int b, int a) {
		mShapeColor[0] = r / 255.0f;
		mShapeColor[1] = g / 255.0f;
		mShapeColor[2] = b / 255.0f;
		mShapeColor[3] = a / 255.0f;
	}
}
