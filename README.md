# SensorTag-CC2650

This project was fork from https://git.ti.com/sensortag-20-android which is the last open source version of the SensorTag Android application for BLE provided by Texas Instruments. 
Due to licensing restrictions Texas Instruments was no longer able to keep the open source code in sync with the SensorTag app in Google Play (see legend on https://git.ti.com/sensortag-20-android ). 

The version on this repository is working with Android Studio 3.1.2 and Android SDK 27. 
Bugs etc are everywhere. 
The cloud connection may be not work (not tested yet!).

Modifications
-----------------
in the `AndroidManifiest.xml`:
* `<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />`
* `<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />`

Android 8.1 needs for BLE location permission. Thus, the following functions were added/modified in `MainActivity.java`:
* `public void onCreate(Bundle savedInstanceState)`
* `public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)`
 
in the file `MainActivity.java` the following function and callback were modified for compatibility:
* `private boolean scanLeDevice(boolean enable)`
* `private ScanCallback mLeScanCallback = new ScanCallback()`
* `boolean checkDeviceFilter(String deviceAddr)`

in the file `strings.xml`:
*  `<string-array name="device_filter"><item>B0:B4:48:</item><item>54:6C:0E:</item><item>68:C9:0B:</item></string-array>` 

Requirements
---------------
* Android Studio (>3.1.2)
* Android Oreo (8.1)
* SensorTag CC2650 (with BLE configuration) - (v1.12 or v1.20)
	- it supports devices with MAC address: 
		- B0:B4:48:
		- 54:6C:0E:
		- 68:C9:0B:
		- [add your under device_filter](https://github.com/lemariva/SensorTag-CC2650/blob/master/BleSensorTag/src/main/res/values/strings.xml)


Changelog
----------------
Revision: 2.4



