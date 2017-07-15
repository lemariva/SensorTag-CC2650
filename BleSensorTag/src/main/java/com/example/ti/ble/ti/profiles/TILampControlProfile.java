package com.example.ti.ble.ti.profiles;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.ti.ble.common.BluetoothLeService;
import com.example.ti.ble.common.CloudProfileConfigurationDialogFragment;
import com.example.ti.ble.common.GenericBluetoothProfile;
import com.example.ti.ble.sensortag.SensorTagGatt;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ole on 22/06/15.
 */
public class TILampControlProfile extends GenericBluetoothProfile {

    private static final String lightService_UUID = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String lightRed_UUID = "0000ffb1-0000-1000-8000-00805f9b34fb";
    private static final String lightGreen_UUID = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private static final String lightBlue_UUID = "0000ffb3-0000-1000-8000-00805f9b34fb";
    private static final String lightWhite_UUID = "0000ffb4-0000-1000-8000-00805f9b34fb";
    private static final String lightCompound_UUID = "0000ffb5-0000-1000-8000-00805f9b34fb";

    TILampControlProfileTableRow cRow;
    BroadcastReceiver lightControlReceiver;
    int R = 10,G = 10 ,B = 10,W = 10;
    BluetoothGattCharacteristic compoundCharacteristic;
    Timer updateLampTimer;

    public TILampControlProfile(Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
        super(con,device,service,controller);
        this.cRow = new TILampControlProfileTableRow(con);
        this.tRow = this.cRow;


        this.cRow.setColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TILampControlDialogFragment dF = TILampControlDialogFragment.newInstance();

                final Activity act = (Activity)context;
                dF.show(act.getFragmentManager(),"LampSetting");


            }
        });

        List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();
        for (BluetoothGattCharacteristic c : characteristics) {

            if (c.getUuid().toString().equals(lightCompound_UUID.toString())) {
                this.compoundCharacteristic = c;
            }
        }




        this.tRow.setIcon(this.getIconPrefix(), service.getUuid().toString());

        lightControlReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(TILampControlDialogFragment.ACTION_LAMP_HSI_COLOR_CHANGED)) {
                    double H,S,I;
                    H = intent.getDoubleExtra(TILampControlDialogFragment.EXTRA_LAMP_HSI_COLOR_H, 0.0f);
                    S = intent.getDoubleExtra(TILampControlDialogFragment.EXTRA_LAMP_HSI_COLOR_S, 0.0f);
                    I = intent.getDoubleExtra(TILampControlDialogFragment.EXTRA_LAMP_HSI_COLOR_I, 0.0f);
                    setLightHSV(H,S,I);
                }
            }
        };
        this.context.registerReceiver(lightControlReceiver, makeTILampBroadcastFilter());


    }

    public void setLightHSV(double H,double S, double I) {

            double cos_h, cos_1047_h;
            H = H % 360.0f; // cycle H around to 0-360 degrees
            H = 3.14159 * H / (float)180; // Convert to radians.
            S = S>0?(S<1?S:1):0; // clamp S and I to interval [0,1]
            I = I>0?(I<1?I:1):0;

            if(H < 2.09439) {
                cos_h = Math.cos(H);
                cos_1047_h = Math.cos(1.047196667 - H);
                R = (int)(S*255*I/3*(1+cos_h/cos_1047_h));
                G = (int)(S*255*I/3*(1+(1-cos_h/cos_1047_h)));
                B = (int)0;
                W = (int)(255*(1-S)*I);
            } else if(H < 4.188787) {
                H = H - 2.09439;
                cos_h = Math.cos(H);
                cos_1047_h = Math.cos(1.047196667-H);
                G = (int)(S*255*I/3*(1+cos_h/cos_1047_h));
                B = (int)(S*255*I/3*(1+(1-cos_h/cos_1047_h)));
                R = 0;
                W = (int)(255*(1-S)*I);
            } else {
                H = H - 4.188787;
                cos_h = Math.cos(H);
                cos_1047_h = Math.cos(1.047196667-H);
                B = (int)(S*255*I/3*(1+cos_h/cos_1047_h));
                R = (int)(S*255*I/3*(1+(1-cos_h/cos_1047_h)));
                G = 0;
                W = (int)(255*(1-S)*I);
            }
        }

    public static boolean isCorrectService(BluetoothGattService service) {
        if ((service.getUuid().toString().compareTo(lightService_UUID)) == 0) {
            return true;
        }
        else return false;
    }
    @Override
    public void onPause() {
        super.onPause();
        this.context.unregisterReceiver(lightControlReceiver);
    }
    @Override
    public void onResume() {
        super.onResume();
        this.context.registerReceiver(lightControlReceiver,makeTILampBroadcastFilter());
    }
    @Override
    public void enableService() {
        this.updateLampTimer = new Timer();
        this.updateLampTimer.schedule(new updateCompoundTask(),1000,100);
    }
    @Override
    public void disableService() {
        this.updateLampTimer.cancel();
        this.updateLampTimer = null;
    }
    @Override
    public void configureService() {

    }
    @Override
    public void deConfigureService() {

    }
    @Override
    public void periodWasUpdated(int period) {

    }

    private static IntentFilter makeTILampBroadcastFilter() {
        final IntentFilter fi = new IntentFilter();
        fi.addAction(TILampControlDialogFragment.ACTION_LAMP_HSI_COLOR_CHANGED);
        return fi;
    }

    private class updateCompoundTask extends TimerTask {
        byte oldR,oldG,oldB,oldW;
        @Override
        public void run() {
            if (compoundCharacteristic != null) {
                byte[] p = {(byte)R,(byte)G,(byte)B,(byte)W};
                if ((p[0] == oldR) && (p[1] == oldG) && (p[2] == oldB) && (p[3] == oldW)) return;
                int error = mBTLeService.writeCharacteristic(compoundCharacteristic, p);
                if (error != 0) {
                    Log.d("TILampControlProfile","Error writing compound color characteristic !");
                }
                oldR = p[0];
                oldG = p[1];
                oldB = p[2];
                oldW = p[3];
            }
        }
    }
}
