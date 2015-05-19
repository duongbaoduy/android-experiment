package org.dbd.exp.android.ui.activity;

import org.dbd.exp.android.ui.BaseActivity;
import org.dbd.exp.android.ui.R;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AudioDevice extends BaseActivity {
	private Thread _testThread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		AudioInit(getBaseContext());
		final Button buttonStart = (Button) findViewById(R.id.Button01);
		// buttonStart.setWidth(200);
		// button.layout(50, 50, 100, 40);
		buttonStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				_testThread = new Thread(_testProc);
				_testThread.start();
			}
		});

		// Suggest to use the voice call audio stream for hardware volume
		// controls
		setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

		DoLog("Started WebRTC Android ADM Test");

	}

	private Runnable _testProc = new Runnable() {
		public void run() {
			// TODO(xians), choose test from GUI
			// Select test here, 0 for API test, 1-> for Func tests
			RunTest(5);
		}
	};

	private void DoLog(String msg) {
		Log.d("*WebRTC ADM*", msg);
	}

	// //////////////// Native function prototypes ////////////////////

	// Init wrapper
	private native static boolean NativeInit();

	// Function used to call test
	private native int RunTest(int testType);

	private native int AudioInit(Context c);

	// Load native library
	static {
		Log.d("*WebRTC ADM*", "Loading audio_device_android_test...");
		System.loadLibrary("audiodev");

		Log.d("*WebRTC ADM*", "Calling native init...");
		if (!NativeInit()) {
			Log.e("*WebRTC ADM*", "Native init failed");
			throw new RuntimeException("Native init failed");
		} else {
			Log.d("*WebRTC ADM*", "Native init successful");
		}
	}
}