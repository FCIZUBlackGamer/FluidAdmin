package com.thetatecno.fluidadmin;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragBlood extends Fragment {
    String color;
    public fragBlood(String s) {
        color = s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty, container, false);
//        LinearLayout linearLayout = view.findViewById(R.id.linear_layout);
//        linearLayout.setBackgroundColor(Color.parseColor(color));
        TextView colorTxt = view.findViewById(R.id.colorTxt);
        colorTxt.setText(color);
        view.setBackgroundColor(Color.parseColor(color));
        Log.e("Color",color);
        return view;
    }
}
