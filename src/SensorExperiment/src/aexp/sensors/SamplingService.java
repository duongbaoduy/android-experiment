package aexp.sensors;

import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.PowerManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.hardware.SensorManager;
import android.util.Config;
import android.util.Log;
import android.util.LogPrinter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Date;

public class SamplingService extends Service implements SensorEventListener {
	static final String LOG_TAG = "SAMPLINGSERVICE";
	static final boolean KEEPAWAKE_HACK = false;
	static final boolean MINIMAL_ENERGY = false;
	static final long MINIMAL_ENERGY_LOG_PERIOD = 15000L;

	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand( intent, flags, startId );
		if( Sensors.DEBUG )
			Log.d( LOG_TAG, "onStartCommand" );
		stopSampling();		// just in case the activity-level service management fails
      	sensorName = intent.getStringExtra( "sensorname" );
      	if( Sensors.DEBUG )
      		Log.d( LOG_TAG,"sensorName: "+sensorName );
        SharedPreferences appPrefs = getSharedPreferences( 
                                        Sensors.PREF_FILE,
                                        MODE_PRIVATE );
        rate = appPrefs.getInt(
                    Sensors.PREF_SAMPLING_SPEED, 
                    SensorManager.SENSOR_DELAY_UI );
        if( Sensors.DEBUG )
        	Log.d( LOG_TAG, "rate: "+rate );

		screenOffBroadcastReceiver = new ScreenOffBroadcastReceiver();
		IntentFilter screenOffFilter = new IntentFilter();
		screenOffFilter.addAction( Intent.ACTION_SCREEN_OFF );
		if( KEEPAWAKE_HACK )
			registerReceiver( screenOffBroadcastReceiver, screenOffFilter );
		sensorManager = (SensorManager)getSystemService( SENSOR_SERVICE  );
		startSampling();
		if( Sensors.DEBUG )
			Log.d( LOG_TAG, "onStartCommand ends" );
		return START_NOT_STICKY;
	}

	public void onDestroy() {
		super.onDestroy();
		if( Sensors.DEBUG )
			Log.d( LOG_TAG, "onDestroy" );
		stopSampling();
		if( KEEPAWAKE_HACK )
			unregisterReceiver( screenOffBroadcastReceiver );
	}

	public IBinder onBind(Intent intent) {
		return null;	// cannot bind
	}

// SensorEventListener
    public void onAccuracyChanged (Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
		++logCounter;
    	if( !MINIMAL_ENERGY ) {
    		if( Sensors.DEBUG ){
    			StringBuilder b = new StringBuilder();
    			for( int i = 0 ; i < sensorEvent.values.length ; ++i ) {
    				if( i > 0 )
    					b.append( " , " );
    				b.append( Float.toString( sensorEvent.values[i] ) );
    			}
    			Log.d( LOG_TAG, "onSensorChanged: "+new Date().toString()+" ["+b+"]" );
    		}
    		if( captureFile != null ) {
    			captureFile.print( Long.toString( sensorEvent.timestamp) );
    			for( int i = 0 ; i < sensorEvent.values.length ; ++i ) {
    				captureFile.print( "," );
    				captureFile.print( Float.toString( sensorEvent.values[i] ) );
    			}
    			captureFile.println();
    		} 
    	} else {
    		++logCounter;
    		if( ( logCounter % MINIMAL_ENERGY_LOG_PERIOD ) == 0L )
    			Log.d( LOG_TAG, "logCounter: "+logCounter+" at "+new Date().toString());
    	}
    }

    
	private void stopSampling() {
		if( !samplingStarted )
			return;
		if( generateUserActivityThread != null ) {
			generateUserActivityThread.stopThread();
			generateUserActivityThread = null;
		}
        if( sensorManager != null ) {
        	if( Config.DEBUG )
        		Log.d( LOG_TAG, "unregisterListener/SamplingService" );
            sensorManager.unregisterListener( this );
		}
        if( captureFile != null ) {
            captureFile.close();
			captureFile = null;
        }
		samplingStarted = false;
		sampingInProgressWakeLock.release();
		sampingInProgressWakeLock = null;
		Date samplingStoppedTimeStamp = new Date();
		long secondsEllapsed = 
			( samplingStoppedTimeStamp.getTime() -
			  samplingStartedTimeStamp.getTime() ) / 1000L;
		Log.d(LOG_TAG, "Sampling started: "+
				samplingStartedTimeStamp.toString()+
				"; Sampling stopped: "+
				samplingStoppedTimeStamp.toString()+
				" ("+secondsEllapsed+" seconds) "+
				"; samples collected: "+logCounter );
	}

	private void startSampling() {
		if( samplingStarted )
			return;
        if( sensorName != null ) {
            List<Sensor> sensors = sensorManager.getSensorList( Sensor.TYPE_ALL );
            ourSensor = null;;
            for( int i = 0 ; i < sensors.size() ; ++i )
                if( sensorName.equals( sensors.get( i ).getName() ) ) {
                    ourSensor = sensors.get( i );
                    break;
                }
            if( ourSensor != null ) {
            	if( Sensors.DEBUG )
            		Log.d( LOG_TAG, "registerListener/SamplingService" );
           		sensorManager.registerListener( 
                            this, 
                            ourSensor,
                            rate );
			}
            samplingStartedTimeStamp = new Date();
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
			sampingInProgressWakeLock = 
				pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SamplingInProgress");
			sampingInProgressWakeLock.acquire();
			captureFile = null;
			if( Sensors.DEBUG )
				Log.d( LOG_TAG, "Capture file created" );
            File captureFileName = new File( "/sdcard", "capture.csv" );
            try {
                captureFile = new PrintWriter( new FileWriter( captureFileName, false ) );
            } catch( IOException ex ) {
                Log.e( LOG_TAG, ex.getMessage(), ex );
            }
			samplingStarted = true;
        }
	}


    private String sensorName;
    private int rate = SensorManager.SENSOR_DELAY_UI;
    private SensorManager sensorManager;
    private PrintWriter captureFile;
	private boolean samplingStarted = false;
	private ScreenOffBroadcastReceiver screenOffBroadcastReceiver = null;
	private Sensor ourSensor;
	private GenerateUserActivityThread generateUserActivityThread = null;
	private long logCounter = 0;
	private PowerManager.WakeLock sampingInProgressWakeLock;
	private Date samplingStartedTimeStamp;

	class ScreenOffBroadcastReceiver extends BroadcastReceiver {
		private static final String LOG_TAG = "ScreenOffBroadcastReceiver";

		public void onReceive(Context context, Intent intent) {
			if( Sensors.DEBUG )
				Log.d( LOG_TAG, "onReceive: "+intent );
			if( sensorManager != null && samplingStarted ) {
				if( generateUserActivityThread != null ) {
					generateUserActivityThread.stopThread();
					generateUserActivityThread = null;
				}
				generateUserActivityThread = new GenerateUserActivityThread();
				generateUserActivityThread.start();
			}
		}
	}

	class GenerateUserActivityThread extends Thread {
		public void run() {
			if( Sensors.DEBUG )
				Log.d( LOG_TAG, "Waiting 2 sec for switching back the screen ..." );
			try {
				Thread.sleep( 2000L );
			} catch( InterruptedException ex ) {}
			if( Sensors.DEBUG )
				Log.d( LOG_TAG, "User activity generation thread started" );

			PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
			userActivityWakeLock = 
				pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, 
						"GenerateUserActivity");
			userActivityWakeLock.acquire();
			if( Sensors.DEBUG )
				Log.d( LOG_TAG, "User activity generation thread exiting" );
		}

		public void stopThread() {
			if( Sensors.DEBUG )
				Log.d( LOG_TAG, "User activity wake lock released" );
			userActivityWakeLock.release();
			userActivityWakeLock = null;
		}

		PowerManager.WakeLock userActivityWakeLock;
	}
}

