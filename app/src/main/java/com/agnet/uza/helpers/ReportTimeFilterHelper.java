package com.agnet.uza.helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.agnet.uza.R;

public class ReportTimeFilterHelper {

    private Context c;

    public  ReportTimeFilterHelper(Context context){
        c = context;
    }

    public void changeBgColor(TextView textView, ViewGroup viewGroup){


        for(int index = 0; index < ((ViewGroup) viewGroup).getChildCount(); index++) {
            TextView nextChild = (TextView) ((ViewGroup) viewGroup).getChildAt(index);

            nextChild.setBackgroundResource(R.drawable.round_corners);
            nextChild.setTextColor(Color.parseColor("#666666"));

        }

        textView.setBackgroundResource(R.drawable.round_corners_primary);
            textView.setTextColor(Color.parseColor("#ffffff"));

    }
}
