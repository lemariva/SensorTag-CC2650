package com.example.ti.ble.ti.profiles;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.ti.ble.sensortag.R;

/**
 * Created by ole on 22/06/15.
 */
public class TILampControlDialogFragment extends DialogFragment {
    public static final String ACTION_LAMP_HSI_COLOR_CHANGED = "org.example.ti.ble.ti.profiles.ACTION_LAMP_HSI_COLOR_CHANGED";
    public static final String EXTRA_LAMP_HSI_COLOR_H = "org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_H";
    public static final String EXTRA_LAMP_HSI_COLOR_S = "org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_S";
    public static final String EXTRA_LAMP_HSI_COLOR_I = "org.example.ti.ble.ti.profiles.EXTRA_LAMP_HSI_COLOR_I";

    View v;
    float downx = 0,downy = 0,upx = 0,upy = 0;
    SeekBar intensityBar;
    double S;
    double H;
    double I;

    public static TILampControlDialogFragment newInstance() {
        TILampControlDialogFragment frag = new TILampControlDialogFragment();
        Bundle args = new Bundle();
        return frag;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder lightDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Set color")
                .setPositiveButton("Close", null);
        LayoutInflater i = getActivity().getLayoutInflater();

        v = i.inflate(R.layout.lamp_control, null);

        ImageView colorPicker = (ImageView)v.findViewById(R.id.colorPicker);
        intensityBar = (SeekBar)v.findViewById(R.id.colorIntensitySeekBar);
        intensityBar.setMax(1000);

        if ((colorPicker != null) && (intensityBar !=null)) {

         intensityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 I = (double)(progress) / 1000.0f;
                 broadCastLightValue();
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {

             }
         });

         colorPicker.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 int action = event.getAction();
                 //Log.d("TILampControlDialogFragment","Max X = " + v.getWidth() + " Max Y =" + v.getHeight());
                 switch (action) {
                     case MotionEvent.ACTION_DOWN:
                         downx = event.getX();
                         downy = event.getY();
                         //Log.d("TILampControlDialogFragment", "Touch down : " + downx + "x" + downy);
                         break;
                     case MotionEvent.ACTION_MOVE:
                         upx = event.getX();
                         upy = event.getY();
                         //canvas.drawLine(downx, downy, upx, upy, paint);
                         //imageView.invalidate();
                         downx = upx;
                         downy = upy;
                         //Log.d("TILampControlDialogFragment", "Touch move : " + upx + "x" + upy);
                         break;
                     case MotionEvent.ACTION_UP:
                         upx = event.getX();
                         upy = event.getY();
                         //canvas.drawLine(downx, downy, upx, upy, paint);
                         //imageView.invalidate();
                         //Log.d("TILampControlDialogFragment", "Touch up : " + upx + "x" + upy);
                         break;
                     case MotionEvent.ACTION_CANCEL:
                         break;
                     default:
                         break;
                 }

                 double x = upx - (v.getWidth() / 2);
                 double y = upy - (v.getHeight() / 2);




                 //Find length, this is what is called saturation

                 S = (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))) / (v.getWidth() / 2);

                 //Find angle, this is what is called HUE

                 H = (((Math.atan2(y, x) * 180) / Math.PI) - 180) * -1;

                 I = (double)(intensityBar.getProgress()) / 1000.0f;

                 H += 90.0f;
                 if (H > 360.0f) H -= 360.0f;

                 if (S > 1.0f) S = 1.0f;
                 if (S < 0.0f) S = 0.0f;

                 Log.d("TILampControlDialogFragment", "S: " + S + " H:" + H + " I:" + I);
                 broadCastLightValue();
                 return true;
             }
         });
        }

        lightDialog.setView(v);
        return lightDialog.create();
    }

    void broadCastLightValue() {
        final Intent intent = new Intent(ACTION_LAMP_HSI_COLOR_CHANGED);
        intent.putExtra(EXTRA_LAMP_HSI_COLOR_H,H);
        intent.putExtra(EXTRA_LAMP_HSI_COLOR_I,I);
        intent.putExtra(EXTRA_LAMP_HSI_COLOR_S,S);
        getActivity().sendBroadcast(intent);
    }

}
