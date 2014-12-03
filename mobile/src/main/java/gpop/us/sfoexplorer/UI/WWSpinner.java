package gpop.us.sfoexplorer.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import gpop.us.sfoexplorer.R;

/**
 * Created by Michael Yoon Huh on 12/3/2014.
 */
public class WWSpinner {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SYSTEM VARIABLES
    private final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.

    /** SPINNER FUNCTIONALITY __________________________________________________________________ **/

    // createAirlineSpinner(): Sets up the custom layout for the spinner dropdown object.
    @SuppressLint("NewApi")
    public void createAirlineSpinner(final Context con, Spinner spin, final String airlines[]) {

        final Typeface spinnerFont = WWFont.getInstance(con).setBigNoodleTypeFace();

        // Initializes and creates a new ArrayAdapter object for the spinner.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(con,
                R.layout.ww_airline_spinner_textview, airlines) {

            // getView(): Sets up the spinner view attributes.
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }

            // getView(): Sets up the spinner attributes for the drop down list.
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {

                // Sets up the infrastructure for the drop down list.
                LayoutInflater inflater = LayoutInflater.from(con);
                View airlineSpinnerDrop = inflater.inflate(R.layout.ww_airline_spinner, parent, false);

                // Sets up the custom font attributes for the spinner's drop down list.
                TextView airlineChoice = (TextView) airlineSpinnerDrop.findViewById(R.id.ww_airline_carrier_spinner_text);
                airlineChoice.setTypeface(spinnerFont);
                airlineChoice.setText(airlines[position]);

                return airlineSpinnerDrop;
            }
        };

        // If the current device runs on Android 4.1 or greater, sets the spinner background to transparent.
        if (api_level > 15) { spin.setPopupBackgroundResource(R.drawable.wet_asphalt_transparent_tile); }
        spin.setAdapter(adapter); // Sets the new spinner object.
    }

    // setAirlineList(): Sets up the list of airline carriers for the spinner.
    public String[] setAirlineList(Context con, String spinnerList) {

        String[] airlineList = new String[0]; // Stores the string array list airline values.
        return con.getResources().getStringArray(R.array.airline_carrier);
    }
}
