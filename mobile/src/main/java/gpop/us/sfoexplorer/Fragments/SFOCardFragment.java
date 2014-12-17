package gpop.us.sfoexplorer.Fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import gpop.us.sfoexplorer.Device.SFODisplay;
import gpop.us.sfoexplorer.Model.SFOEventModel;
import gpop.us.sfoexplorer.UI.SFOFont;
import gpop.us.sfoexplorer.UI.SFOImages;
import it.sephiroth.android.library.picasso.Picasso;
import gpop.us.sfoexplorer.R;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */
public class SFOCardFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // EVENT VARIABLES
    private int cardNumber = 0; // References the fragment number.
    private SFOEventModel eventModel; // References the event model object for this fragment.

    // FRAGMENT VARIABLES
    private View card_view; // References the layout for the fragment.

    // LAYOUT VARIABLES
    private ImageView card_background_image, main_card_image; // References the ImageView objects in the fragment.
    private int main_card_image_resource; // Used to reference the main card image.

    // LOGGING VARIABLES
    private static final String TAG = SFOCardFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.
    
    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static SFOCardFragment card_fragment = new SFOCardFragment();

    // WWCardFragment(): Deconstructor for the WWCardFragment.
    public SFOCardFragment() {}

    // getInstance(): Returns the card_fragment instance.
    public static SFOCardFragment getInstance() { return card_fragment; }

    // initializeFragment(): Initializes the fragment with the event properties.
    public void initializeFragment(int cardID, SFOEventModel event) {
        cardNumber = cardID;
        eventModel = event;
    }

    /** FRAGMENT LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.currentActivity = activity; // Sets the currentActivity to attached activity object.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setUpDisplayParameters(); // Sets up the device's display parameters.

        // Sets the view to the specified XML layout file.
        card_view = (ViewGroup) inflater.inflate(R.layout.ww_card_fragment, container, false);
        setUpLayout(); // Sets up the layout for the fragment.

        return card_view;
    }

    // onActivityCreated(): Runs after the attached activity's onCreate() function has been completed.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initiates a first-time call through the interface to initialize the connection between
        // the fragment and the activity.
        updateActivity(false);
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

    /** FRAGMENT EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // onConfigurationChanged(): If the screen orientation changes, this function loads the proper
    // layout, as well as updating all layout-related objects.
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        setUpDisplayParameters(); // Sets up the device's display parameters.
        setUpLayout(); // Sets up the layout for the fragment.
    }

    /** ACTIVITY FUNCTIONALITY _________________________________________________________________ **/

    // updateActivity(): Interfaces with the attached activity class and signals that properties
    // have been changed.
    private void updateActivity(Boolean showDetails) {

        // Signals to the attached activity that the card details fragment needs to be opened.
        try { ((OnCardSelectedListener) currentActivity).updateFromCardFragment(showDetails); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the fragment.
    private void setUpLayout() {

        setUpCard(); // Sets up the card fragment layout objects.
        setUpButtons(); // Sets up the buttons for the fragment.
    }

    // setUpButtons(): Sets up the buttons for the fragment.
    private void setUpButtons() {

        /*
        // References the ImageButton objects.
        //ImageButton card_details_button = (ImageButton) card_view.findViewById(R.id.card_details_button);

        // Sets up the listener and the actions for the invisible card details button.
        card_details_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true); // Signals to the attached activity that the details fragment should be shown.
            }
        });
        */

        LinearLayout card_event_text_container = (LinearLayout) card_view.findViewById(R.id.card_event_text_container);

        // Sets up the listener and the actions for the invisible card details button.
        card_event_text_container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true); // Signals to the attached activity that the details fragment should be shown.
            }
        });
    }

    // setUpImages(): Sets up the images for the fragment.
    private void setUpCard() {

        // References the ImageView objects.
        card_background_image = (ImageView) card_view.findViewById(R.id.card_background_image);
        main_card_image = (ImageView) card_view.findViewById(R.id.card_activity_image);
        ImageView eta_indicator_icon = (ImageView) card_view.findViewById(R.id.card_indicator_icon);

        // References the TextView objects.
        TextView event_title = (TextView) card_view.findViewById(R.id.card_event_name);
        TextView event_location = (TextView) card_view.findViewById(R.id.card_event_location);
        TextView event_location_label = (TextView) card_view.findViewById(R.id.card_terminal_text);
        TextView event_gate = (TextView) card_view.findViewById(R.id.card_event_gate);
        TextView event_gate_label = (TextView) card_view.findViewById(R.id.card_gate_text);
        TextView event_eta = (TextView) card_view.findViewById(R.id.card_eta_text);
        TextView event_eta_spacer = (TextView) card_view.findViewById(R.id.card_eta_spacer);

        // Sets the custom font type to the TextView objects.
        event_gate.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace());
        event_gate_label.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace());
        event_location.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace());
        event_location_label.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace());
        event_title.setTypeface(SFOFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace());
        event_eta.setTypeface(SFOFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace());
        event_eta_spacer.setTypeface(SFOFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace());

        // Sets up a shadow effect for the TextView objects.
        event_title.setShadowLayer(8, 4, 4, Color.BLACK);
        event_gate.setShadowLayer(8, 4, 4, Color.BLACK);
        event_gate_label.setShadowLayer(8, 4, 4, Color.BLACK);
        event_location.setShadowLayer(8, 4, 4, Color.BLACK);
        event_location_label.setShadowLayer(8, 4, 4, Color.BLACK);
        event_eta.setShadowLayer(8, 4, 4, Color.BLACK);

        // Retrieves the event details from the WWEventModel object.
        String event_name = eventModel.getEventTitle(); // Gets the event name from the JSON string.
        String event_gate_number = eventModel.getGateLocation(); // Gets the gate location from the JSON string.
        String event_terminal = eventModel.getTerminalLocation(); // Gets the terminal location from the JSON string.
        String event_image_url = eventModel.getEventImageURL(); // Gets the event image URL from the JSON string.

        Log.d(TAG, "Card: " + cardNumber + " | Image URL: " + event_image_url); // Logging.

        // Sets the event image for the ImageView object.
        Picasso.with(currentActivity)
                .load(event_image_url)
                .withOptions(SFOImages.setBitmapOptions())
                .fit()
                .centerCrop()
                .into(card_background_image);

        String location_spacer = " AT"; // Normally used if the gate value is not -1.

        // Hides the gate details if the value is equal to -1.
        if (event_gate_number.equals("-1")) {
            event_gate.setVisibility(View.GONE); // Hides the TextView object.
            //location_spacer = "";
        }

        // Removes improper text from the string for formatting.
        event_terminal = event_terminal.replace("Terminal", ""); // Removes 'Terminal' from the string.
        event_terminal = event_terminal.replace("TERMINAL", ""); // Removes 'Terminal' from the string.
        event_gate_number = event_gate_number.replace("Boarding Area ", "");
        event_gate_number = event_gate_number.replace("Boarding area ", "");
        event_gate_number = event_gate_number.replace("Near ", "");
        event_gate_number = event_gate_number.replace("Gate", "");

        // Sets the event text for the TextView objects.
        event_title.setText(event_name);
        //event_location.setText("IN " + event_terminal + location_spacer);
        event_location.setText(event_terminal);
        //event_gate.setText("GATE " + event_gate_number);
        event_gate.setText(event_gate_number);
    }

    /** RESOLUTION FUNCTIONALITY _______________________________________________________________ **/

    // setUpDisplayParameters(): Sets up the device's display parameters.
    private void setUpDisplayParameters() {

        // References the display parameters for the device.
        Display deviceWindow = currentActivity.getWindowManager().getDefaultDisplay();

        currentOrientation = SFODisplay.updateDisplayLayout(currentActivity, deviceWindow); // Retrieves the device's display attributes.
        resolutionDimens = SFODisplay.getResolution(deviceWindow);
        displaySize = SFODisplay.getDisplaySize(resolutionDimens, currentOrientation);
    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    /** --------------------------------------------------------------------------------------------
     *  [OnCardSelectedListener] INTERFACE
     *  DESCRIPTION: An interface that is used by activities attached to this fragment to determine
     *  if buttons in the fragment have been clicked and if preference values have been changed.
     *  --------------------------------------------------------------------------------------------
     */

    public interface OnCardSelectedListener{
        public void updateFromCardFragment(Boolean showDetails);
    }
}