package com.example.neyser.emailtracking.common;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

/**
 * Created by Juan Carlos on 08/08/2017.
 */

public class SpinnerHelper {

    public static void populate(Context context, Spinner spinnerTarget, List<String> options) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, options);
        spinnerTarget.setAdapter(spinnerArrayAdapter);
    }

    public static void setSelectedOption(Spinner spinner, String option)
    {
        for (int i=0; i<spinner.getCount(); i++)
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(option)){
                spinner.setSelection(i);
                break;
            }
    }

    public static int getSelectedIndex(Spinner spinner)
    {
        final String selectedOption = spinner.getSelectedItem().toString();
        int index = 0;

        for (int i=0; i<spinner.getCount(); i++)
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(selectedOption)){
                index = i;
                break;
            }

        return index;
    }


}
