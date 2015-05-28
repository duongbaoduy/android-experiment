package aexp.sensors;

import java.util.ArrayList;
import java.util.List;

import org.dbd.exp.android.dev.R;

import android.app.ListActivity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Sensors extends ListActivity
{
    public static final String PREF_CAPTURE_STATE = "captureState";
    public static final String PREF_SAMPLING_SPEED = "samplingSpeed";
    public static final boolean DEBUG = true;
    public static final String PREF_FILE = "prefs";
    static final int MENU_SETTINGS = 1;
    static final String LOG_TAG = "SENSORS";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		if( savedInstanceState != null ) {
			samplingServicePosition = savedInstanceState.getInt( SAMPLING_SERVICE_POSITION_KEY, -1 );
			samplingServiceRunning = samplingServicePosition >= 0;	
			Log.d(LOG_TAG, "reinitialized samplingServiceRunning: "+samplingServiceRunning);
		}
        setContentView(R.layout.main);
        SensorManager sensorManager = 
                (SensorManager)getSystemService( SENSOR_SERVICE  );
        ArrayList<SensorItem> items = new ArrayList<SensorItem>();
        List<Sensor> sensors = sensorManager.getSensorList( Sensor.TYPE_ALL );
        for( int i = 0 ; i < sensors.size() ; ++i )
            items.add( new SensorItem( sensors.get( i ) ) );
        if( samplingServiceRunning ) {
        	SensorItem item = items.get( samplingServicePosition );
        	item.setSampling( true );
        }
        ListView lv = getListView();
        listAdapter = new SensorListAdapter( this, items);
        lv.setAdapter(  listAdapter );
// Set up the long click handler
		lv.setOnItemLongClickListener( 
			new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(
					AdapterView<?> av, 
					View v, 
					int pos, 
					long id) {
                        onLongListItemClick(v,pos,id);
                        return true;
        		}
			});
    }

// Save the information that 
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt( SAMPLING_SERVICE_POSITION_KEY, samplingServicePosition ); 
	}

	protected void onDestroy() {
		super.onDestroy();
		if( Sensors.DEBUG )
			Log.d( LOG_TAG, "onDestroy" );
	}

	protected void onLongListItemClick( View v, int pos, long id) {
		if( Sensors.DEBUG )
			Log.d( LOG_TAG, "onLongListItemClick pos: "+pos+"; id: " + id );
// If sampling is running on another sensor
		if( samplingServiceRunning && ( pos != samplingServicePosition ) )
			startSamplingService( pos );
		else
// If sampling is running on the same sensor
		if( samplingServiceRunning && ( pos == samplingServicePosition ) )
			stopSamplingService();
		else
// If no sampling is running then just start the sampling on the sensor
		if( !samplingServiceRunning )
			startSamplingService( pos );
	}


    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_SETTINGS, 1, R.string.menu_settings );
        return result;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
        switch( id ) {
            case MENU_SETTINGS:
                Intent i = new Intent();
                i.setClassName( "aexp.sensors","aexp.sensors.SensorSettings" );
                startActivity( i );
                break;

        }
        return true;
    }

    protected void onListItemClick(
            ListView l,
            View v,
            int position,
            long id) {
        Sensor sensor = ((SensorItem)listAdapter.getItem( position )).getSensor();
        String sensorName = sensor.getName();
        Intent i = new Intent();
        i.setClassName( "aexp.sensors","aexp.sensors.SensorMonitor" );
        i.putExtra( "sensorname",sensorName );
        startActivity( i );
    }

	private void startSamplingService( int position ) {
		stopSamplingService();
		SensorItem item = (SensorItem)listAdapter.getItem( position );
        Sensor sensor = item.getSensor();
        String sensorName = sensor.getName();
        Intent i = new Intent();
        i.setClassName( "aexp.sensors","aexp.sensors.SamplingService" );
        i.putExtra( "sensorname",sensorName );
        startService( i );
		samplingServiceRunning = true;				
		samplingServicePosition = position;
		item.setSampling( true );
		listAdapter.notifyDataSetChanged();
	}

	private void stopSamplingService() {
		if( samplingServiceRunning ) {
        	Intent i = new Intent();
        	i.setClassName( "aexp.sensors","aexp.sensors.SamplingService" );
        	stopService( i );
    		SensorItem item = (SensorItem)listAdapter.getItem( samplingServicePosition );
    		item.setSampling( false );
			samplingServiceRunning = false;
			samplingServicePosition = -1;
			listAdapter.notifyDataSetChanged();
		}
	}

    private SensorListAdapter listAdapter;
	private boolean samplingServiceRunning = false;
	private int samplingServicePosition = 0;
	private static final String SAMPLING_SERVICE_POSITION_KEY = "samplingServicePositon";
}
