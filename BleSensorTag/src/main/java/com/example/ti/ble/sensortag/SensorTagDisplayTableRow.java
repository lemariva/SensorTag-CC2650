/**************************************************************************************************
 Filename:       SensorTagDisplayTableRow.java

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

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.ti.util.GenericCharacteristicTableRow;

public class SensorTagDisplayTableRow extends GenericCharacteristicTableRow {

    EditText displayText;
    CheckBox displayInvert;
    CheckBox displayClock;

    SensorTagDisplayTableRow(Context con) {
        super(con);
        this.title.setText("Display control");

        this.displayText = new EditText(con);
        this.displayText.setMaxLines(1);
        this.displayText.setInputType(InputType.TYPE_CLASS_TEXT);
        this.displayText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.displayText.setId(600);
        int maxLength = 16;
        this.displayText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        this.displayInvert = new CheckBox(con);
        this.displayInvert.setText("Invert display");
        this.displayInvert.setId(601);

        this.displayClock = new CheckBox(con);
        this.displayClock.setText("Clock mode");
        this.displayClock.setId(602);

        RelativeLayout.LayoutParams displayTextLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        displayTextLayout.addRule(RelativeLayout.BELOW,
                this.title.getId());
        displayTextLayout.setMargins(70, 30, 30, 20);
        displayTextLayout.addRule(RelativeLayout.RIGHT_OF, icon.getId());
        this.displayText.setLayoutParams(displayTextLayout);

        RelativeLayout.LayoutParams displayInvertLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        displayInvertLayout.setMargins(30, 10, 0, 30);
        displayInvertLayout.addRule(RelativeLayout.BELOW, this.displayText.getId());
        displayInvertLayout.addRule(RelativeLayout.RIGHT_OF, icon.getId());
        this.displayInvert.setLayoutParams(displayInvertLayout);

        RelativeLayout.LayoutParams displayClockLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        displayClockLayout.setMargins(30,10,0,30);
        displayClockLayout.addRule(RelativeLayout.BELOW,this.displayText.getId());
        displayClockLayout.addRule(RelativeLayout.RIGHT_OF, displayInvert.getId());
        this.displayClock.setLayoutParams(displayClockLayout);

        /* Remove most of the normal controls */

        this.rowLayout.removeAllViews();
        this.rowLayout.addView(this.title);
        this.rowLayout.addView(this.icon);
        this.rowLayout.addView(this.displayText);
        this.rowLayout.addView(this.displayInvert);
        this.rowLayout.addView(this.displayClock);
    }

}
