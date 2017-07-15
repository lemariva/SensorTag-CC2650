/**************************************************************************************************
 Filename:       firmwareEntryTableRow.java

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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ti.ble.sensortag.R;

public class firmwareEntryTableRow extends TableRow {
    private final Paint linePaint;
    protected final RelativeLayout rowLayout;
    TextView subTitleView;
    TextView tV;
    tiFirmwareEntry ent;
    public int position;
    public firmwareEntryTableRow(Context con,tiFirmwareEntry entry) {
        super(con);
        this.ent = entry;
        this.linePaint = new Paint() {
            {
                setStrokeWidth(1);
                setARGB(255, 0, 0, 0);
            }
        };
        this.rowLayout = new RelativeLayout(con);

        tV = new TextView(con);
        tV.setId(500);
        if (tV != null) {
            tV.setText(String.format("%s %1.2f %s(%s)",entry.BoardType,entry.Version,entry.DevPack + " ",entry.WirelessStandard));
        }

        tV.setPadding(10,5,10,5);
        tV.setTextSize(20);
        //tV.setTypeface(Typeface.DEFAULT_BOLD);
        RelativeLayout.LayoutParams tmpLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tmpLayoutParams.addRule(RelativeLayout.BELOW, tV.getId());


        subTitleView = new TextView(con);
        if (subTitleView != null) {
            if (entry.compatible) subTitleView.setText(String.format("%s","Compatible"));
            else subTitleView.setText("Not compatible");
        }
        subTitleView.setTextSize(12);
        subTitleView.setLayoutParams(tmpLayoutParams);
        subTitleView.setPadding(10,5,10,5);

        this.rowLayout.addView(tV);
        this.rowLayout.addView(subTitleView);
        this.addView(this.rowLayout);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, canvas.getHeight() - this.linePaint.getStrokeWidth(), canvas.getWidth(), canvas.getHeight() - this.linePaint.getStrokeWidth(), this.linePaint);
    }

    public void setGrayedOut(Boolean grayed) {
        if (grayed == true) {
            tV.setTextColor(Color.LTGRAY);
            subTitleView.setTextColor(Color.LTGRAY);
        }
        else {
            tV.setTextColor(Color.BLACK);
            subTitleView.setTextColor(Color.BLACK);
        }
    }
}
