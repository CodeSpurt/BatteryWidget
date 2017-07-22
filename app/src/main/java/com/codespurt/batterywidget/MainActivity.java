package com.codespurt.batterywidget;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;

public class MainActivity extends AppCompatActivity {

    private CircleProgress batteryLevel;
    private TextView batteryChargingStatus;
    private Intent batteryStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryLevel = (CircleProgress) findViewById(R.id.battery_level);
        batteryChargingStatus = (TextView) findViewById(R.id.battery_charging_status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBatteryLevel(getBatteryLevel());
        setBatteryStatus(batteryStatus);
    }

    // current battery level
    private int getBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = this.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        return level;
    }

    private void setBatteryLevel(int level) {
        batteryLevel.setProgress(level);
    }

    private void setBatteryStatus(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

        if (isCharging) {
            // other charging status
            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            if (usbCharge) {
                batteryChargingStatus.setText(getResources().getString(R.string.battery_status) + " : Charging via USB");
            } else if (acCharge) {
                batteryChargingStatus.setText(getResources().getString(R.string.battery_status) + " : Charging via AC");
            } else {
                batteryChargingStatus.setText(getResources().getString(R.string.battery_status) + " : Charging");
            }
        } else {
            batteryChargingStatus.setText(getResources().getString(R.string.battery_status) + " : Dis-charging");
        }
    }
}
