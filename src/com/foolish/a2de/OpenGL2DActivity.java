package com.foolish.a2de;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.foolish.opengl_2d_engine.R;

public class OpenGL2DActivity extends Activity {

	protected FrameLayout mSurfaceContainer;
	protected OpenGL2DSurfaceView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		makeFullscreen();

		setContentView(R.layout.gl_surface_controls_overlay);

		mSurfaceContainer = (FrameLayout) findViewById(R.id.surfaceContainer);
		mGLView = new OpenGL2DSurfaceView(this);
		mSurfaceContainer.addView(mGLView);
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

}
