package com.example.earthquake;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    private static final String LOG_TAG = WordAdapter.class.getSimpleName();
    private int mColor;
    public WordAdapter(Activity context, ArrayList<Word> words){
        super(context,0,words);

    }
    public int getMagnitudeColor(double mMagnitude){
        int mag=(int)mMagnitude,ans=0;

        //int magnitude1Color = ContextCompat.getColor(getContext(), R.color.magnitude1);
        switch (mag){
            case 0:
            case 1:
            case 2: ans=ContextCompat.getColor(getContext(), R.color.magnitude1); break;
            case 3:
            case 4: ans=ContextCompat.getColor(getContext(), R.color.magnitude2); break;
            case 5:
            case 6: ans=ContextCompat.getColor(getContext(), R.color.magnitude3); break;
            case 7: ans=ContextCompat.getColor(getContext(), R.color.magnitude4); break;
            case 8: ans=ContextCompat.getColor(getContext(), R.color.magnitude5); break;
            case 9: ans=ContextCompat.getColor(getContext(), R.color.magnitude7); break;
            case 10: ans=ContextCompat.getColor(getContext(), R.color.magnitude8); break;
            default: ans=ContextCompat.getColor(getContext(), R.color.magnitude7); break;
        }
        return ans;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        View listItemview=convertView;
        if(listItemview==null)
        {
            listItemview= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Word current=getItem(position);

        TextView mag=(TextView)listItemview.findViewById(R.id.magnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();   // magnitude'S Background

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(current.getmMagnitude());;

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
        mag.setText(current.getmMagnitude()+"");
        TextView dist=(TextView)listItemview.findViewById(R.id.distance);
        dist.setText(current.getmDistance());

        TextView loc=(TextView)listItemview.findViewById(R.id.location);
        loc.setText(current.getmLocation());

        TextView date=(TextView)listItemview.findViewById(R.id.date);
        date.setText(current.getmDate());

        TextView time=(TextView)listItemview.findViewById(R.id.time);
        time.setText(current.getmTime());


        return listItemview;

    }
}
/**
////   Adding Image For DIfferent Magnitude
import android.support.v4.content.ContextCompat;

        …

private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
        case 0:
        case 1:
        magnitudeColorResourceId = R.color.magnitude1;
        break;
        case 2:
        magnitudeColorResourceId = R.color.magnitude2;
        break;
        case 3:
        magnitudeColorResourceId = R.color.magnitude3;
        break;
        case 4:
        magnitudeColorResourceId = R.color.magnitude4;
        break;
        case 5:
        magnitudeColorResourceId = R.color.magnitude5;
        break;
        case 6:
        magnitudeColorResourceId = R.color.magnitude6;
        break;
        case 7:
        magnitudeColorResourceId = R.color.magnitude7;
        break;
        case 8:
        magnitudeColorResourceId = R.color.magnitude8;
        break;
        case 9:
        magnitudeColorResourceId = R.color.magnitude9;
        break;
default:
        magnitudeColorResourceId = R.color.magnitude10plus;
        break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
        }
        **/