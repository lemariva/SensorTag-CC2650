/**************************************************************************************************
 Filename:       SensorTagDisplayProfile.java

 Copyright (c) 2013 - 2015 Texas Instruments Incorporated

 All rights reserved not granted herein.
 Limited License.

 Texas Instruments Incorporated grants a world-wide, royalty-free,
 non-exclusive license under copyrights and patents it now or hereafter
 owns or controls to make, have made, use, import, offer to sell and sell ("Utilize")
 this software subject to the terms herein.  With respect to the foregoing patent
 license, such license is granted  solely to the extent that any such patent is necessary
 to Utilize the software alone.  The patent license shall not apply to any combinations which
 include this software, other than combinations with devices manufactured by or for TI ('TI Devices').
 No hardware patent is licensed hereunder.

 Redistributions must preserve existing copyright notices and reproduce this license (including the
 above copyright notice and the disclaimer and (if applicable) source code license limitations below)
 in the documentation and/or other materials provided with the distribution

 Redistribution and use in binary form, without modification, are permitted provided that the following
 conditions are met:

 * No reverse engineering, decompilation, or disassembly of this software is permitted with respect to any
 software provided in binary form.
 * any redistribution and use are licensed by TI for use only with TI Devices.
 * Nothing shall obligate TI to provide you with source code for the software licensed and provided to you in object code.

 If software source code is provided to you, modification and redistribution of the source code are permitted
 provided that the following conditions are met:

 * any redistribution and use of the source code, including any resulting derivative works, are licensed by
 TI for use only with TI Devices.
 * any redistribution and use of any object code compiled from the source code and any resulting derivative
 works, are licensed by TI for use only with TI Devices.

 Neither the name of Texas Instruments Incorporated nor the names of its suppliers may be used to endorse or
 promote products derived from this software without specific prior written permission.

 DISCLAIMER.

 THIS SOFTWARE IS PROVIDED BY TI AND TI'S LICENSORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL TI AND TI'S LICENSORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.


 **************************************************************************************************/
package com.example.ti.ble.sensortag;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;

import com.example.ti.ble.common.BluetoothLeService;
import com.example.ti.ble.common.GenericBluetoothProfile;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SensorTagDisplayProfile extends GenericBluetoothProfile {
    public static final String TI_SENSORTAG_TWO_DISPLAY_SERVICE_UUID = "f000ad00-0451-4000-b000-000000000000";
    public static final String TI_SENSORTAG_TWO_DISPLAY_DATA_UUID = "f000ad01-0451-4000-b000-000000000000";
    public static final String TI_SENSORTAG_TWO_DISPLAY_CONTROL_UUID = "f000ad02-0451-4000-b000-000000000000";
    SensorTagDisplayTableRow cRow;
    Timer displayClock;

    public SensorTagDisplayProfile(Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
        super(con, device, service, controller);
        this.cRow = new SensorTagDisplayTableRow(con);
        this.tRow = this.cRow;

        List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();

        for (BluetoothGattCharacteristic c : characteristics) {
            if (c.getUuid().toString().equals(TI_SENSORTAG_TWO_DISPLAY_DATA_UUID)) {
                this.dataC = c;
            }
            if (c.getUuid().toString().equals(TI_SENSORTAG_TWO_DISPLAY_CONTROL_UUID)) {
                this.configC = c;
            }
        }
        this.tRow.setIcon(this.getIconPrefix(), this.dataC.getUuid().toString());

        this.cRow.displayClock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (displayClock != null) {
                        displayClock.cancel();
                    }
                    displayClock = new Timer();
                    displayClock.schedule(new clockTask(),1000,1000);
                }
                else {
                    if (displayClock != null) {
                        displayClock.cancel();
                    }
                }
            }
        });

        this.cRow.displayInvert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (configC != null) {
                    byte b = 0x05;
                    int error = mBTLeService.writeCharacteristic(configC, b);
                    if (error != 0) {
                        Log.d("SensorTagDisplayProfile", "Error writing config characteristic !");
                    }
                }
            }
        });

        this.cRow.displayText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("SensorTagDisplayProfile", "New Display Text:" + s);
                byte[] p = new byte[s.length()];
                for (int ii = 0; ii < s.length(); ii++) {
                    p[ii] = (byte) s.charAt(ii);
                }
                if (dataC != null) {
                    int error = mBTLeService.writeCharacteristic(dataC, p);
                    if (error != 0) {
                        Log.d("SensorTagDisplayProfile", "Error writing data characteristic !");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    public static boolean isCorrectService(BluetoothGattService service) {
        if ((service.getUuid().toString().compareTo(TI_SENSORTAG_TWO_DISPLAY_SERVICE_UUID.toString())) == 0) {
            return true;
        }
        else return false;
    }

    @Override
    public void enableService () {
        if (this.cRow.displayClock.isChecked()) {
            if (displayClock != null) {
                displayClock.cancel();
            }
            displayClock = new Timer();
            displayClock.schedule(new clockTask(),1000,1000);
        }
    }
    @Override
    public void disableService () {
        if (displayClock != null) {
            displayClock.cancel();
        }
    }
    @Override
    public void configureService() {

    }
    @Override
    public void deConfigureService() {

    }
    @Override
    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic c) {
    }

    private class clockTask extends TimerTask {
        @Override
        public void run() {
            Date d = new Date();
            final String date = String.format("%02d:%02d:%02d        ",d.getHours(),d.getMinutes(),d.getSeconds());
            byte[] b = new byte[date.length()];
            for (int ii = 0; ii < date.length(); ii++) {
                b[ii] = (byte)date.charAt(ii);
            }
            if (dataC != null) {
                Activity a = (Activity)context;
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cRow.displayText.setText(date);
                    }
                });
            }
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
