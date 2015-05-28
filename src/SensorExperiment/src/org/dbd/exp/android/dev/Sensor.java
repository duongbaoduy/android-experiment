package org.dbd.exp.android.dev;

public class Sensor {
	public static native int SensorInit();

	public static native int RunTest(int type);
}
