package gpop.us.sfoexplorer.Fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import gpop.us.sfoexplorer.Device.WWDisplay;
import gpop.us.sfoexplorer.Model.WWFlightModel;
import gpop.us.sfoexplorer.R;
import gpop.us.sfoexplorer.UI.WWFont;
import gpop.us.sfoexplorer.UI.WWImages;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Michael Yoon Huh on 11/20/2014.
 */
public class WWDepartureFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // FLIGHT VARIABLES
    private String carrier; // References the airline carrier of the flight.
    private WWFlightModel flightModel; // References the event model object for this fragment.

    // FRAGMENT VARIABLES
    private View departure_view; // References the layout for the fragment.

    // LOGGING VARIABLES
    private static final String TAG = WWDepartureFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static WWDepartureFragment departure_fragment = new WWDepartureFragment();

    // WWWeatherFragment(): Deconstructor for the WWWeatherFragment.
    public WWDepartureFragment() {}

    // getInstance(): Returns the departure_fragment instance.
    public static WWDepartureFragment getInstance() { return departure_fragment; }

    // initializeFragment(): Initializes the fragment with the event properties.
    public void initializeFragment(WWFlightModel flight, String airline) {
        flightModel = flight;
        carrier = airline;
    }

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
        departure_view = (ViewGroup) inflater.inflate(R.layout.ww_flight_fragment, container, false);
        setUpLayout(); // Sets up the layout for the fragment.

        return departure_view;
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
    private void updateActivity(Boolean hideFlight) {

        // Signals to the attached activity that the card details fragment needs to be closed.
        try { ((OnFlightSelectedListener) currentActivity).updateFromFlightFragment(hideFlight); }
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

        setUpButtons(); // Sets up the Button objects for the fragment.
        setUpImages(); // Sets up the ImageView objects for the fragment.
        setUpText(); // Sets up the TextView objects for the fragment.

    }

    // setUpButtons(): Sets up the button objects for the fragment.
    private void setUpButtons() {

        // References the ImageButton objects in the layout.
        ImageButton back_button = (ImageButton) departure_view.findViewById(R.id.flight_back_button);

        // Sets up the listener and the actions for the back button.
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true); // Signals to the attached activity that the details fragment should be shown.
            }
        });

    }

    // setUpImages(): Sets up the ImageView objects for the fragment.
    private void setUpImages() {

        // References the ImageView objects.
        ImageView carrier_logo = (ImageView) departure_view.findViewById(R.id.airline_carrier_image);

        // Determines the airline carrier logo based on the flight.
        int carrier_logo_resource = R.drawable.aa_icon; // Sets the American Airlines logo.

        // Sets the weather icon for the ImageView object.
        Picasso.with(currentActivity)
                .load(carrier_logo_resource)
                .withOptions(WWImages.setBitmapOptions())
                .into(carrier_logo);
    }

    // setUpText(): Sets up the TextView objects for the fragment.
    private void setUpText() {

        // References the TextView objects.
        TextView airline_carrier = (TextView) departure_view.findViewById(R.id.airline_carrier_text);
        TextView flight_number = (TextView) departure_view.findViewById(R.id.flight_number_text);
        TextView gate_number = (TextView) departure_view.findViewById(R.id.gate_text);

        // Sets up the custom font type for the TextView objects.
        airline_carrier.setTypeface(WWFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace());// Sets the custom font face.
        flight_number.setTypeface(WWFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace()); // Sets the custom font face.
        gate_number.setTypeface(WWFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.

        // Sets up a shadow effect for the TextView objects.
        airline_carrier.setShadowLayer(8, 4, 4, Color.BLACK);
        flight_number.setShadowLayer(8, 4, 4, Color.BLACK);
        gate_number.setShadowLayer(8, 4, 4, Color.BLACK);

        // Retrieves the flight parameters from the WWFlightModel object.
        int timeToDeparture = flightModel.getTimeToDeparture(); // Gets the time to departure value from the JSON string.
        String departureGate = flightModel.getDepartureGate(); // Gets the departure gate from the JSON string.
        String departureTime = flightModel.getDepartureTime(); // Gets the departure time from the JSON string.

        // Sets the weather status for the TextView object.
        airline_carrier.setText(carrier); // Sets the airline carrier text.
        gate_number.setText(departureGate); // Sets the departure gate text.
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
     *  [OnFlightSelectedListener] INTERFACE
     *  DESCRIPTION: An interface that is used by activities attached to this fragment to determine
     *  if buttons in the fragment have been clicked and if preference values have been changed.
     *  --------------------------------------------------------------------------------------------
     */

    public interface OnFlightSelectedListener{
        public void updateFromFlightFragment(Boolean hideDetails);
    }
}
