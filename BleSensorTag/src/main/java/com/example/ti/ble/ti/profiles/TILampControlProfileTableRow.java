package com.example.ti.ble.ti.profiles;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.ti.ble.sensortag.R;
import com.example.ti.util.GenericCharacteristicTableRow;

/**
 * Created by ole on 22/06/15.
 */
public class TILampControlProfileTableRow extends GenericCharacteristicTableRow {

    public Button setColorButton;

    TILampControlProfileTableRow(Context con) {
        super(con);

        setColorButton = new Button(con);
        setColorButton.setText("Set color");

        RelativeLayout.LayoutParams setColorButtonLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        setColorButtonLayout.addRule(RelativeLayout.BELOW,
                this.title.getId());
        setColorButtonLayout.setMargins(70,30,30,20);
        setColorButtonLayout.addRule(RelativeLayout.RIGHT_OF, icon.getId());
        this.setColorButton.setLayoutParams(setColorButtonLayout);

        this.title.setText("Light Control");





        /* Remove most of the normal controls */

        this.rowLayout.removeAllViews();
        this.rowLayout.addView(this.title);
        this.rowLayout.addView(this.icon);
        this.rowLayout.addView(setColorButton);





    }


}
