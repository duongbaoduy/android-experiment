package aexp.sensors;

import android.hardware.Sensor;

class SensorItem {
    SensorItem( Sensor sensor ) {
        this.sensor = sensor;
        this.sampling = false;
    }

    public String getSensorName() {
        return sensor.getName();
    }

    Sensor getSensor() {
        return sensor;
    }

    void setSampling( boolean sampling ) {
    	this.sampling = sampling;
    }
    
    boolean getSampling() {
    	return sampling;
    }
    
    private Sensor sensor;
    private boolean sampling;
}
