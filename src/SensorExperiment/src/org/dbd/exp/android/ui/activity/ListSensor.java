package org.dbd.exp.android.ui.activity;

import java.util.List;

import org.dbd.exp.android.dev.R;
import org.dbd.exp.android.ui.BaseActivity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class ListSensor extends BaseActivity {

	private SensorManager mSensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_sensor);

		TextView sensorsData = (TextView) findViewById(R.id.textView1);

		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		List<Sensor> list = mSensorManager.getSensorList(Sensor.TYPE_ALL);

		StringBuilder data = new StringBuilder();
		for (Sensor sensor : list) {
			data.append(sensor.getName() + "\n");
			data.append(sensor.getVendor() + "\n");
			data.append(sensor.getVersion() + "\n");

		}
		sensorsData.setText(data);
	}
}
