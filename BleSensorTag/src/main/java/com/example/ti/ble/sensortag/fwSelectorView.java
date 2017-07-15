/**************************************************************************************************
 Filename:       fwSelectorView.java

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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ti.util.firmwareEntryTableRow;
import com.example.ti.util.tiFirmwareEntry;

import java.util.List;

public class fwSelectorView extends DialogFragment {
    public final static String ACTION_FW_WAS_SELECTED = "com.example.ti.ble.sensortag.fwSelectorView.SELECTED";
    public final static String EXTRA_SELECTED_FW_INDEX = "com.example.ti.ble.sensortag.fwSelectorView.EXTRA_SELECTED_FW_INDEX";
    List<tiFirmwareEntry> firmwares = null;
    TableLayout table = null;
    float cFW;

    public static fwSelectorView newInstance(List<tiFirmwareEntry> firmwareEntries,float currentFW) {
        fwSelectorView frag = new fwSelectorView();
        frag.firmwares = firmwareEntries;
        frag.cFW = currentFW;
        Bundle args = new Bundle();
        Log.d("fwSelectorView","Current firmware version : " + currentFW);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder fwDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Select Factory FW")
                .setNegativeButton("Cancel", null);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View fwView = inflater.inflate(R.layout.fw_selector, null);

        table = (TableLayout) fwView.findViewById(R.id.fwentries_layout);
        table.removeAllViews();
        /* Initialize view from file */
        if (this.firmwares != null) {
            for(int ii = 0; ii < this.firmwares.size(); ii++) {
                tiFirmwareEntry entry = this.firmwares.get(ii);
                if (entry.RequiredVersionRev > cFW) {
                    entry.compatible = false;
                }
                if (entry.Version < cFW) {
                    entry.compatible = false;
                }
                final firmwareEntryTableRow tRow = new firmwareEntryTableRow(getActivity(),entry);
                GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[] { Color.WHITE, Color.LTGRAY});
                g.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                StateListDrawable states = new StateListDrawable();
                states.addState(new int[] {android.R.attr.state_pressed,-android.R.attr.state_selected},g);

                tRow.setBackgroundDrawable(states);
                tRow.position = ii;
                tRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("fwSelectorView", "Firmware cell clicked");
                        final Intent intent = new Intent(ACTION_FW_WAS_SELECTED);
                        intent.putExtra(EXTRA_SELECTED_FW_INDEX,tRow.position);
                        getActivity().sendBroadcast(intent);
                        dismiss();
                    }
                });
                if (entry.compatible == true)tRow.setGrayedOut(false);
                else tRow.setGrayedOut(true);
                table.addView(tRow);
                table.requestLayout();

            }

        }
        fwDialog.setView(fwView);
        Dialog fwSelectorDialog = fwDialog.create();
        return fwSelectorDialog;
    }
}
