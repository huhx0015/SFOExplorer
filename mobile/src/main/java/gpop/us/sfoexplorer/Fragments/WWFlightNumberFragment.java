package gpop.us.sfoexplorer.Fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gpop.us.sfoexplorer.Device.WWDisplay;
import gpop.us.sfoexplorer.Model.WWWeatherModel;
import gpop.us.sfoexplorer.R;
import gpop.us.sfoexplorer.UI.WWFont;
import gpop.us.sfoexplorer.UI.WWImages;
import gpop.us.sfoexplorer.Data.WWWeather;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class WWFlightNumberFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // FRAGMENT VARIABLES
    private View flight_number_view; // References the layout for the fragment.

    // LAYOUT VARIABLES
    private Button enter_flight_button; // References the Button object.
    private EditText enter_flight_input; // References the EditText input object.

    // LOGGING VARIABLES
    private static final String TAG = WWFlightNumberFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static WWFlightNumberFragment flight_fragment = new WWFlightNumberFragment();

    // WWFlightNumberFragment(): Deconstructor for the WWFlightNumberFragment.
    public WWFlightNumberFragment() {}

    // getInstance(): Returns the details_fragment instance.
    public static WWFlightNumberFragment getInstance() { return flight_fragment; }

    /** FRAGMENT LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.currentActivity = activity; // Sets the currentActivity to attached activity object.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setUpDisplayParameters(); // Sets up the device's display parameters.

        // Sets the view to the specified XML layout file.
        flight_number_view = (ViewGroup) inflater.inflate(R.layout.ww_flight_number_fragment, container, false);
        setUpLayout(); // Sets up the layout for the fragment.

        return flight_number_view;
    }

    // onActivityCreated(): Runs after the attached activity's onCreate() function has been completed.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initiates a first-time call through the interface to initialize the connection between
        // the fragment and the activity.
        //updateActivity();
    }

    // onResume(): This function runs immediately after onCreate() finishes and is always re-run
    // whenever the activity is resumed from an onPause() state.
    @Override
    public void onResume() {
        super.onResume();
    }

    // onPause(): This function is called whenever the current fragment is suspended.
    @Override
    public void onPause() {
        super.onPause();
    }

    /** ACTIVITY FUNCTIONALITY _________________________________________________________________ **/

    // updateActivity(): Interfaces with the attached activity class and signals that properties
    // have been changed.
    private void updateActivity(Boolean isReturn, String flightNumber) {

        // Signals to the attached activity that the card details fragment needs to be closed.
        try { ((OnFlightNumberSelectedListener) currentActivity).updateFromFlightNoFragment(isReturn, flightNumber); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    /** FRAGMENT EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // onConfigurationChanged(): If the screen orientation changes, this function loads the proper
    // layout, as well as updating all layout-related objects.
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        setUpDisplayParameters(); // Sets up the device's display parameters.
        setUpLayout(); // Sets up the layout for the fragment.
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the activity.
    private void setUpLayout() {
        setUpText(); // Sets up the TextView objects for the fragment.
        setUpButtons(); // Sets up the Button objects for the fragment.
    }

    // setUpButtons(): Sets up the button objects for the fragment.
    private void setUpButtons() {

        // References the ImageButton objects in the layout.
        ImageButton back_button = (ImageButton) flight_number_view.findViewById(R.id.flight_back_button);

        // Sets up the listener and the actions for the back button.
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, null); // Signals to the attached activity to close this fragment.
            }
        });

        // Sets up the listener and the actions for the back button.
        enter_flight_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String flightInput = null; // References the flight input.

                try { flightInput = enter_flight_input.getText().toString(); } // Converts the input to a String object.
                catch(NullPointerException e) { e.printStackTrace(); } // NullPointerException handler.

                // If the flight input is not null, the main activity is signalled to close this fragment.
                if (flightInput != null) { updateActivity(true, flightInput); } // Signals to the attached activity to close this fragment.

                // Otherwise, a Toast is displayed to inform the user about entering in their flight number.
                else {
                    Toast.makeText(currentActivity, "Please input your flight number.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // setUpText(): Sets up the TextView objects for the fragment.
    private void setUpText() {

        // References the Button, EditText, and TextView objects.
        enter_flight_button = (Button) flight_number_view.findViewById(R.id.enter_flight_button);
        TextView enter_flight_text = (TextView) flight_number_view.findViewById(R.id.enter_flight_text);
        enter_flight_input = (EditText) flight_number_view.findViewById(R.id.enter_flight_number_input);

        // Sets up the custom font type for the TextView objects.
        enter_flight_button.setTypeface(WWFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        enter_flight_input.setTypeface(WWFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        enter_flight_text.setTypeface(WWFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.

        // Sets up a shadow effect for the TextView objects.
        enter_flight_button.setShadowLayer(8, 4, 4, Color.BLACK);
        enter_flight_text.setShadowLayer(8, 4, 4, Color.BLACK);
    }

    /** RESOLUTION FUNCTIONALITY _______________________________________________________________ **/

    // setUpDisplayParameters(): Sets up the device's display parameters.
    private void setUpDisplayParameters() {

        // References the display parameters for the device.
        Display deviceWindow = currentActivity.getWindowManager().getDefaultDisplay();

        currentOrientation = WWDisplay.updateDisplayLayout(currentActivity, deviceWindow); // Retrieves the device's display attributes.
        resolutionDimens = WWDisplay.getResolution(deviceWindow);
        displaySize = WWDisplay.getDisplaySize(resolutionDimens, currentOrientation);
    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    /** --------------------------------------------------------------------------------------------
     *  [OnFlightNumberSelectedListener] INTERFACE
     *  DESCRIPTION: An interface that is used by activities attached to this fragment to determine
     *  if buttons in the fragment have been clicked and if preference values have been changed.
     *  --------------------------------------------------------------------------------------------
     */

    public interface OnFlightNumberSelectedListener {
        public void updateFromFlightNoFragment(Boolean isReturn, String flightNumber);
    }
}
