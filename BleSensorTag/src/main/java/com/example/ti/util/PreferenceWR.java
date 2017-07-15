/**************************************************************************************************
 Filename:       PreferenceWR.java

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
package com.example.ti.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


public class PreferenceWR {
    public static final String PREFERENCEWR_NEEDS_REFRESH = "refresh";
    private SharedPreferences sharedPreferences;
    private String prefix;

    public PreferenceWR(String BluetoothAddress,Context con) {
        super();
        this.prefix = BluetoothAddress;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(con);
        this.setBooleanPreference("Exists",true);
        Log.d("PreferenceWR","Instantiated a new preference reader/writer with prefix : \"" + this.prefix + "_\"");
    }

    public static boolean isKnown(String BluetoothAddress,Context con) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(con);
        return s.getBoolean(BluetoothAddress + "_" + "Exists",false);
    }

    /* String settings */
    public String getStringPreference(String prefName) {
        String defaultValue = "NS";
        return this.sharedPreferences.getString(this.prefix + "_" + prefName, defaultValue);
    }

    public boolean setStringPreference(String prefName, String prefValue) {
        SharedPreferences.Editor ed = this.sharedPreferences.edit();
        ed.putString(this.prefix + "_" + prefName, prefValue);
        return ed.commit();
    }

    /* Boolean settings */

    public boolean getBooleanPreference(String prefName) {
        boolean defaultValue = false;
        return this.sharedPreferences.getBoolean(this.prefix + "_" + prefName, defaultValue);
    }

    public boolean setBooleanPreference(String prefName, boolean prefValue) {
        SharedPreferences.Editor ed = this.sharedPreferences.edit();
        ed.putBoolean(this.prefix + "_" + prefName, prefValue);
        return ed.commit();
    }

    /* Integer settings */

    public int getIntegerPreference(String prefName) {
        int defaultValue = -1;
        return this.sharedPreferences.getInt(this.prefix + "_" + prefName, -1);
    }

    public boolean setIntegerPreference(String prefName,int prefValue) {
        SharedPreferences.Editor ed = this.sharedPreferences.edit();
        ed.putInt(this.prefix + "_" + prefName, prefValue);
        return ed.commit();
    }
}
