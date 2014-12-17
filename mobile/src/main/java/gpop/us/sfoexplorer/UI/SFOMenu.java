package gpop.us.sfoexplorer.UI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import gpop.us.sfoexplorer.Data.SFOAirlineCodes;
import gpop.us.sfoexplorer.R;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Michael Yoon Huh on 12/16/2014.
 */

public class SFOMenu {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LAYOUT VARIABLES
    private Button commandButton_1, commandButton_2, commandButton_3, commandButton_4, commandButton_5; // Used for referencing the command window buttons.
    private Button dialogButton_1, dialogButton_2; // Used for referencing the dialog window buttons.
    private TextView dialogHeader, dialogText; // Used for referencing the dialog header and text TextView objects.

    // SYSTEM VARIABLES
    private final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.

    /** MENU FUNCTIONALITY _____________________________________________________________________ **/

    // createDialogWindow(): Initializes and creates a notice dialog window. It builds the base
    // dialog structure and returns the Dialog object.
    public Dialog createDialogWindow(Context con, String activity) {

        // Dialog window initialization and setup.
        final Dialog dg_dialog_builder = new Dialog(con, R.style.WWDialog);
        LayoutInflater inflater = LayoutInflater.from(con);
        View dgDialog = inflater.inflate(R.layout.ww_dialog_notice, null);
        dg_dialog_builder.setContentView(dgDialog);

        // Assigns references to the Button and TextView objects.
        dialogHeader = (TextView) dgDialog.findViewById(R.id.ww_header_text);
        dialogText = (TextView) dgDialog.findViewById(R.id.ww_notice_text);
        dialogButton_1 = (Button) dgDialog.findViewById(R.id.dialog_button_1);

        // Sets the custom font properties to the dialog window buttons.
        dialogHeader.setTypeface(SFOFont.getInstance(con).setRobotoRegular());
        dialogButton_1.setTypeface(SFOFont.getInstance(con).setRobotoLight());

        return dg_dialog_builder; // Returns the dialog object.
    }

    // createAirlineSpinner(): Sets up the custom layout for the spinner dropdown object.
    public static void createAirlineSpinner(final Context con, Spinner spin, final String airlines[]) {

        final Typeface spinnerFont = SFOFont.getInstance(con).setBigNoodleTypeFace();

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
                View spinnerDrop = inflater.inflate(R.layout.ww_airline_spinner, parent, false);

                // Decodes the airlineIcon ImageView object for memory optimization purposes.
                ImageView airlineIcon = (ImageView) spinnerDrop.findViewById(R.id.ww_world_spinner_icon);
                int airlineIconResource = getAirlineLogo(position);
                Picasso.with(con)
                        .load(airlineIconResource)
                        .resize(48, 48)
                        .centerCrop()
                        .withOptions(SFOImages.setBitmapOptions())
                        .into(airlineIcon);

                // Sets up the custom font attributes for the spinner's drop down list.
                TextView mapChoice = (TextView) spinnerDrop.findViewById(R.id.ww_world_spinner_choice);
                mapChoice.setTypeface(spinnerFont);
                mapChoice.setText(airlines[position]);

                return spinnerDrop;
            }
        };

        spin.setAdapter(adapter); // Sets the new spinner object.
    }

    // getAirlinesLogo(): Retrieves the airline logo for the spinner list elements.
    public static int getAirlineLogo(int position) {

        int airlineLogo = R.drawable.plane_icon; // Default logo for the airline drop list element.

        switch (position) {

            // AMERICAN AIRLINES:
            case 0:
                airlineLogo = SFOAirlineCodes.getAirlineIcon("AA");
                break;

            // CONTINENTAL AIRLINES:
            case 1:
                airlineLogo = SFOAirlineCodes.getAirlineIcon("CO");
                break;

            // DELTA AIRLINES:
            case 2:
                airlineLogo = SFOAirlineCodes.getAirlineIcon("DL");
                break;

            // SOUTHWEST AIRLINES:
            case 3:
                airlineLogo = SFOAirlineCodes.getAirlineIcon("WN");
                break;

            // US AIRWAYS:
            case 4:
                airlineLogo = SFOAirlineCodes.getAirlineIcon("US");
                break;

            // VIRGIN AMERICA:
            case 5:
                airlineLogo = SFOAirlineCodes.getAirlineIcon("VX");
                break;
        }

        return airlineLogo;
    }

    /** ADDITIONAL FUNCTIONALITY _______________________________________________________________ **/

    // setAirlineList(): Sets up the list of airline for the spinner.
    public static String[] setAirlineList(Context con, String spinnerList) {

        String[] airlineList = new String[0]; // Stores the string array list airline values.

        // TERMINAL 1:
        if (spinnerList.equals("terminal_1")) {
            airlineList = con.getResources().getStringArray(R.array.sfo_terminal_2_airline_list);
        }

        // TERMINAL 2:
        else if (spinnerList.equals("terminal_2")) {
            airlineList = con.getResources().getStringArray(R.array.sfo_terminal_2_airline_list);
        }

        // TERMINAL 3:
        else if (spinnerList.equals("terminal_3")) {
            airlineList = con.getResources().getStringArray(R.array.sfo_terminal_2_airline_list);
        }

        // INTERNATIONAL TERMINAL:
        else if (spinnerList.equals("international_terminal")) {
            airlineList = con.getResources().getStringArray(R.array.sfo_terminal_2_airline_list);
        }

        return airlineList;
    }
}