package com.foolish.a2de;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.foolish.a2de.graphics.Sprite;
import com.foolish.opengl_2d_engine.R;

public class OpenGL2DActivity extends Activity {

	protected FrameLayout mSurfaceContainer;
	protected OpenGL2DSurfaceView mGLView;
	protected ImageView mControlA, mControlB, mControlLeft, mControlRight;
	float speedX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		makeFullscreen();

		setContentView(R.layout.gl_surface_controls_overlay);

		mSurfaceContainer = (FrameLayout) findViewById(R.id.surfaceContainer);
		mGLView = new OpenGL2DSurfaceView(this);
		mSurfaceContainer.addView(mGLView);

		mControlA = (ImageView) findViewById(R.id.controlA);
		mControlB = (ImageView) findViewById(R.id.controlB);
		mControlLeft = (ImageView) findViewById(R.id.controlLeft);
		mControlRight = (ImageView) findViewById(R.id.controlRight);

		initControlListeners();
	}

	private void initControlListeners() {
		mControlA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Sprite sprite = mGLView.mRenderer.mSprite;
				if (sprite.isGrounded()) {
					sprite.setGrounded(false);
					sprite.setSpeed(sprite.speed().x, 0.03f);
				}
			}
		});
		mControlB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		mControlLeft.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Sprite sprite = mGLView.mRenderer.mSprite;

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					speedX = -0.03f;
					break;
				case MotionEvent.ACTION_UP:
					speedX = 0.0f;
					break;
				}

				sprite.setSpeed(speedX, sprite.speed().y);

				return true;
			}
		});
		mControlRight.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Sprite sprite = mGLView.mRenderer.mSprite;

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					speedX = 0.03f;
					break;
				case MotionEvent.ACTION_UP:
					speedX = 0.0f;
					break;
				}

				sprite.setSpeed(speedX, sprite.speed().y);

				return true;
			}
		});
	}

	protected void makeFullscreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			makeFullscreenJellybean();
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	protected void makeFullscreenJellybean() {
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		getWindow().getDecorView().setSystemUiVisibility(uiOptions);
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected void makeFullescreenKitkat() {
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
			makeFullescreenKitkat();
		}
	}
}
