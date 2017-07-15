# SensorTag-CC2650

This project was fork from https://git.ti.com/sensortag-20-android which is the last open source version of the SensorTag Android application for BLE provided from Texas Instruments. 
Due to licensing restrictions Texas Instruments was no longer able to keep the open source code in sync with the SensorTag app in Google Play (see legend on https://git.ti.com/sensortag-20-android ). 

The version on this repository is working with Android Studio 2.3.3 and Android SDK 25. 
Bugs etc are everywhere. 
The cloud connection may be not work (not tested yet!).

Modifications
-----------------
in the file `MainActivity.java` the following function and callback were modified for compatibility:
* `private boolean scanLeDevice(boolean enable)`
* `private ScanCallback mLeScanCallback = new ScanCallback()`
* `boolean checkDeviceFilter(String deviceAddr)`

in the file `strings.xml`:
*  `<string-array name="device_filter"><item>B0:B4:48:</item></string-array>` 

The MAC address `B0:B4:48:` corresponds to Texas Instruments vendor.

Changelog
----------------
Revision: 2.3



