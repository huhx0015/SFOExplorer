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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import gpop.us.sfoexplorer.Device.SFODisplay;
import gpop.us.sfoexplorer.Model.SFOEventModel;
import gpop.us.sfoexplorer.R;
import gpop.us.sfoexplorer.UI.SFOFont;
import gpop.us.sfoexplorer.UI.SFOImages;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class SFODetailsFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // EVENT VARIABLES
    private int cardNumber = 0; // References the fragment number.
    private SFOEventModel eventModel; // References the event model object for this fragment.

    // FRAGMENT VARIABLES
    private View details_view; // References the layout for the fragment.

    // LAYOUT VARIABLES
    private ImageView card_background_image, main_card_image; // References the ImageView objects in the fragment.
    private int main_card_image_resource; // Used to reference the main card image.

    // LOGGING VARIABLES
    private static final String TAG = SFODetailsFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static SFODetailsFragment details_fragment = new SFODetailsFragment();

    // WWDetailsFragment(): Deconstructor for the WWDetailsFragment.
    public SFODetailsFragment() {}

    // getInstance(): Returns the details_fragment instance.
    public static SFODetailsFragment getInstance() { return details_fragment; }

    // initializeFragment(): Initializes the fragment with the event properties.
    public void initializeFragment(int cardID, SFOEventModel event) {
        cardNumber = cardID;
        eventModel = event;
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
        details_view = (ViewGroup) inflater.inflate(R.layout.ww_details_fragment, container, false);
        setUpLayout(); // Sets up the layout for the fragment.

        return details_view;
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
    private void updateActivity(Boolean hideDetails) {

        // Signals to the attached activity that the card details fragment needs to be closed.
        try { ((OnDetailsSelectedListener) currentActivity).updateFromDetailsFragment(hideDetails); }
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
        ImageButton back_button = (ImageButton) details_view.findViewById(R.id.details_back_button);

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
        ImageView airport_map = (ImageView) details_view.findViewById(R.id.airport_map_display);
        ImageView bonus_code_1 = (ImageView) details_view.findViewById(R.id.card_details_bonus_code_1);
        ImageView bonus_code_2 = (ImageView) details_view.findViewById(R.id.card_details_bonus_code_2);

        String map_image_url = eventModel.getEventMap(); // Gets the map image URL from the WWEventModel object.

        Log.d(TAG, "Card: " + cardNumber + " | Map URL: " + map_image_url); // Logging.

        // Sets the map image for the ImageView object.
        Picasso.with(currentActivity)
                .load(map_image_url)
                .withOptions(SFOImages.setBitmapOptions())
                .fit()
                .centerCrop()
                .into(airport_map);
    }

    // setUpText(): Sets up the TextView objects for the fragment.
    private void setUpText() {

        // References the TextView objects.
        TextView event_name = (TextView) details_view.findViewById(R.id.details_event_name);
        TextView event_location = (TextView) details_view.findViewById(R.id.details_event_location);
        TextView event_gate = (TextView) details_view.findViewById(R.id.details_event_gate);
        TextView event_description = (TextView) details_view.findViewById(R.id.card_details_event_description);
        TextView bonus_text = (TextView) details_view.findViewById(R.id.card_details_bonus_text);

        // Sets up the custom font type for the TextView objects.
        event_name.setTypeface(SFOFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace());// Sets the custom font face.
        event_location.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        event_description.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        event_gate.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        bonus_text.setTypeface(SFOFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace()); // Sets the custom font face.

        // Sets up a shadow effect for the TextView objects.
        event_name.setShadowLayer(8, 4, 4, Color.BLACK);
        event_location.setShadowLayer(8, 4, 4, Color.BLACK);
        event_location.setShadowLayer(8, 4, 4, Color.BLACK);
        event_description.setShadowLayer(8, 4, 4, Color.BLACK);
        bonus_text.setShadowLayer(8, 4, 4, Color.BLACK);

        // Retrieves the event details from the WWEventModel object.
        String event_name_text = eventModel.getEventTitle(); // Gets the event name from the JSON string.
        String event_gate_number = eventModel.getGateLocation(); // Gets the gate location from the JSON string.
        String event_terminal = eventModel.getTerminalLocation(); // Gets the terminal location from the JSON string.
        String event_description_text = eventModel.getEventDescription(); // Gets the event description from the JSON string.

        String location_spacer = " AT"; // Normally used if the gate value is not -1.

        // Hides the gate details if the value is equal to -1.
        if (event_gate_number.equals("-1")) {
            event_gate.setVisibility(View.GONE); // Hides the TextView object.
            location_spacer = "";
        }

        // Removes any HTML code from the description String.
        event_description_text = android.text.Html.fromHtml(event_description_text).toString();

        // Sets the event text for the TextView objects.
        event_name.setText(event_name_text);
        event_location.setText("IN " + event_terminal + location_spacer);
        event_gate.setText("GATE " + event_gate_number);
        event_description.setText(event_description_text);
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
     *  [OnDetailsSelectedListener] INTERFACE
     *  DESCRIPTION: An interface that is used by activities attached to this fragment to determine
     *  if buttons in the fragment have been clicked and if preference values have been changed.
     *  --------------------------------------------------------------------------------------------
     */

    public interface OnDetailsSelectedListener{
        public void updateFromDetailsFragment(Boolean hideDetails);
    }
}
